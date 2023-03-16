'use strict'
const { Router } = require('express');
const api = Router();
var usuarioController = require('../controllers/usuarioC');


api.post('/find_user/', usuarioController.findUser);
api.get('/find_user_by_email/:correo', usuarioController.findUserByEmail);
api.post('/insert_user/', usuarioController.insertUser);
api.put('/update_user/:id', usuarioController.updateUser);
api.delete('/delete_user/:id', usuarioController.deleteUser);

module.exports = api;