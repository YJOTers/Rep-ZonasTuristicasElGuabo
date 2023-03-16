'use strict'
const util = require('util');
const connection = require('../bd/db');
const query = util.promisify(connection.query).bind(connection);
const bcrypt = require('bcrypt');

function isEmptyObject(obj) {
    return !Object.keys(obj).length;
}

//Encontrar y obtener usuario por nombre o correo y clave
const findUser = async function(req, res){
    const nombre = req.body.nombre;
    const clave = req.body.clave;
    const sql1 = `SELECT clave FROM usuarios WHERE nombre = ${connection.escape(nombre)} OR 
                                                   correo = ${connection.escape(nombre)}`;
    const reg1 = await query(sql1);
    if(isEmptyObject(reg1)){
        res.status(200).send(reg1[0]);
    }else{
        const data = Object.values(JSON.parse(JSON.stringify(reg1)));
        const clave_hash = data[0].clave;
        const esLaClave = bcrypt.compareSync(clave, clave_hash);
        if(esLaClave)
        {
            const sql = `SELECT * FROM usuarios WHERE nombre = ${connection.escape(nombre)} OR 
                                                      correo = ${connection.escape(nombre)} AND 
                                                      clave = ${connection.escape(clave_hash)}`;
            const reg = await query(sql);
            res.status(200).send(reg[0]);
        }
    }
}

//Encontrar y obtener usuario correo
const findUserByEmail = async function(req, res){
    const correo = req.params.correo;
    const sql = `SELECT * FROM usuarios WHERE correo = ${connection.escape(correo)}`;
    const reg = await query(sql);
    res.status(200).send(reg[0]);
}

//Insertar usuario
const insertUser = async function(req, res){
    req.body.clave = bcrypt.hashSync(req.body.clave, 10);
    const sql = `INSERT INTO usuarios SET ${connection.escape(req.body)}`;
    const reg = await query(sql);
    res.status(200).send(reg);
}

//Actualizar usuario
const updateUser = async function(req, res){
    const id = req.params.id;
    const clave = req.body.clave;
    const sql1 = `SELECT clave FROM usuarios WHERE id = ${connection.escape(id)}`;
    const reg1 = await query(sql1);
    if(isEmptyObject(reg1)){
        res.status(200).send(reg1);
    }else{
        const data = Object.values(JSON.parse(JSON.stringify(reg1)));
        const clave_hash = data[0].clave;
        if(clave != clave_hash)
        {
            req.body.clave = bcrypt.hashSync(clave, 10);
        }
        const sql = `UPDATE usuarios SET ${connection.escape(req.body)} WHERE id = ${connection.escape(id)}`;
        const reg = await query(sql);
        res.status(200).send(reg);
    }
}

//Eliminar usuario
const deleteUser = async function(req, res){
    const id = req.params.id;
    const sql = `DELETE FROM usuarios WHERE id = ${connection.escape(id)}`;
    const reg = await query(sql);
    res.status(200).send(reg);
}

module.exports = {
    findUser,
    findUserByEmail,
    insertUser,
    updateUser,
    deleteUser
};