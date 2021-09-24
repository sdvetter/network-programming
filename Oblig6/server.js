const http = require('http');
const static = require('node-static');
const file = new static.Server('./');
const crypto = require('crypto');


const webServer = http.createServer((req, res) => {
    req.addListener('end', () => file.serve(req, res)).resume();
});
const port = 3001;
webServer.listen(port, () => console.log(`webserver running at http://localhost:${port}`));
let sockets = []; 

webServer.on('upgrade', (req, socket) => {

    // add all sockets to array
    sockets.push(socket);
    // Return if not upgrade request
    if (req.headers['upgrade'] !== 'websocket') {
        socket.end('HTTP/1.1 400 Bad Request');
        return;
    }

    const acceptKey = req.headers['sec-websocket-key'];
    const hash = generateAcceptValue(acceptKey); 

    // Signifies switching protocol 
    const responseHeaders = ['HTTP/1.1 101 Switching Protocols',
        'Upgrade: WebSocket', 'Connection: Upgrade', `Sec-WebSocket-Accept: ${hash}`];

    socket.write(responseHeaders.join('\r\n') + '\r\n\r\n');

  
    socket.on('data', buffer => {
        const message = parseMessage(buffer);

        if (message) {
            // Write to all connected units
            for (let i of sockets) {
                i.write(constructReply(message));
            }
         // socket.write(constructReply(message));
        } else if (message === null) {
            console.log('WebSocket connection closed by the client.');
        }
    });
    
});




function constructReply(data) {
    const replyString = data.toString(); 
    const stringLength = Buffer.byteLength(replyString);

    const lengthByteCount  = stringLength < 126 ? 0 : 2;
    const payloadLength = lengthByteCount  === 0 ? stringLength : 126;
    const buffer = Buffer.alloc(2 + lengthByteCount  + stringLength);
   
    buffer.writeUInt8(0b10000001, 0);
    buffer.writeUInt8(payloadLength, 1);

    let payloadOffset = 2;
    if (lengthByteCount  > 0) {
        buffer.writeUInt16BE(stringLength, 2); payloadOffset += lengthByteCount ;
    }
    buffer.write(replyString, payloadOffset);
    return buffer;
}

function generateAcceptValue(acceptKey) {
    return crypto
        .createHash('sha1')
        .update(acceptKey + '258EAFA5-E914-47DA-95CA-C5AB0DC85B11', 'binary')
        .digest('base64');
}

function parseMessage(buffer) {
    console.log(buffer);
    const firstByte = buffer.readUInt8(0);
   
    const opCode = firstByte & 0xF;

    // Frame indicates termination of connection  
    if (opCode === 0x8) return null;

    // Not a text frame
    if (opCode !== 0x1) return;

    const secondByte = buffer.readUInt8(1);
    const isMasked = Boolean(secondByte >>> 7);

    let currentOffset = 2;
    let payloadLength = secondByte & 0x7F;
  
    // Keep track of our current position as we advance through the buffer 
    
    if (payloadLength > 125) {
        if (payloadLength === 126) {
            payloadLength = buffer.readUInt16BE(currentOffset);
            currentOffset += 2;
        } else {
          throw new Error('Large payloads not currently implemented');
        }
    }

    let maskingKey;
    if (isMasked) {
        maskingKey = buffer.readUInt32BE(currentOffset);
        currentOffset += 4;
    }

    const data = Buffer.alloc(payloadLength);
    // Only unmask the data if the masking bit was set to 1
    if (isMasked) {
        for (let i = 0, j = 0; i < payloadLength; ++i, j = i % 4) {
            const shift = j == 3 ? 0 : (3 - j) << 3;
            const mask = (shift == 0 ? maskingKey : (maskingKey >>> shift)) & 0xFF;
            const source = buffer.readUInt8(currentOffset++);
            data.writeUInt8(mask ^ source, i);
        }
    } else {
        buffer.copy(data, 0, currentOffset++);
    }

    return data.toString(); 

}