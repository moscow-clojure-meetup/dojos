'use strict';

const http = require('http');
const Express = require('express');

const app = Express();
const port = process.env.PORT || 8080;


app.use(Express.static('public'));

http
  .createServer(app)
  .listen(port, () => console.log('Http server started'));
