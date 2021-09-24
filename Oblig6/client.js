
// Simple HTTP server responds with a simple WebSocket client test

let ws = new WebSocket('ws://localhost:3001');
ws.onmessage = event => alert('Echo from server ' + event.data);
// ws.onopen = () => ws.send('hello');
          


