const express = require('express');

const bodyParser = require('body-parser');
const app = express()
const fs = require('fs');

let path = require('path');
const { exec } = require("child_process");
app.use(bodyParser.urlencoded({extended: true}));
app.use(express.json());
app.use(express.static(path.join(__dirname)));

app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'index.html'));
});


app.post('/compile', function(req, res) {
        fs.writeFile('./cpp/main.cpp', req.body.code , function (err) {
        if (err) return console.log("error writing to file");
        console.log("file has been written");
        exec("docker build \"./cpp/\" -t gcc", () => {
            exec("docker run --rm gcc", (err, stdout, sterr) =>{
                res.status(200).send(JSON.stringify({ans:stdout}))
             });
        });
    });
});


let server = app.listen(5000,function() {});






















