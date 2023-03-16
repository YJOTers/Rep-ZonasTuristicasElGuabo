//Instanciar rutas
var usuarioRoute = require('./src/routes/usuarioR');

//Configuracion de BD
const express = require('express')
const app = express()
require('./src/bd/db')
app.set('port', process.env.PORT || 3000)

//Middlewares
app.use(express.json())

//Usar rutas
app.use('/api', usuarioRoute)

//Escuchar servicio
app.listen(app.get('port'), (error) => {
    if (error) {
        console.log(`Sucedió un error: ${error}`);
    }else{
        console.log(`Servidor corriendo en el puerto: ${app.get('port')}`);
    }
});