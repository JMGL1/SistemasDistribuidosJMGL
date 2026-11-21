const axios = require('axios');
const net = require('net');
const readline = require('readline');

const GRAPHQL_URL = 'http://localhost:4000/graphql';
const SOCKET_HOST = 'localhost';
const SOCKET_PORT = 7777;

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

function preguntar(pregunta) {
    return new Promise(resolve => rl.question(pregunta, resolve));
}

async function verificarBoleta(codigo, ciConductor) {
    const query = `
        query {
            verificarBoleta(
                codigo: "${codigo}"
                ciConductor: "${ciConductor}"
            ) {
                codigo
                nombreConductor
                tipoInfraccion
                lugar
                fecha
                monto
                estado
                habilitadaParaPago
                mensaje
            }
        }
    `;

    const respuesta = await axios.post(GRAPHQL_URL, { query });
    return respuesta.data.data.verificarBoleta;
}

function registrarPago(codigo) {
    return new Promise((resolve, reject) => {
        const socket = new net.Socket();

        socket.connect(SOCKET_PORT, SOCKET_HOST, () => {
            socket.write('PAGAR|' + codigo + '\n');
        });

        socket.on('data', (datos) => {
            const respuesta = datos.toString().trim();
            socket.write('SALIR\n');
            socket.destroy();
            resolve(respuesta);
        });

        socket.on('error', (err) => {
            reject(new Error('Error de conexión con el servidor Socket: ' + err.message));
        });
    });
}

async function main() {
    console.log('ENTIDAD RECAUDADORA - SISTEMA DE PAGO DE MULTAS');

    while (true) {
        console.log('\n--- NUEVA TRANSACCIÓN ---');

        const codigo = await preguntar('Ingrese el Código de Boleta: ');
        const ciConductor = await preguntar('Ingrese el CI del conductor: ');

        if (!codigo.trim() || !ciConductor.trim()) {
            console.log('Código y CI son requeridos.');
            continue;
        }

        try {
            const resultado = await verificarBoleta(codigo.trim(), ciConductor.trim());

            console.log('\nRESULTADO DE VERIFICACIÓN:');
            console.log('Código: ' + (resultado.codigo || '-'));
            console.log('Conductor: ' + (resultado.nombreConductor || '-'));
            console.log('Infracción: ' + (resultado.tipoInfraccion || '-'));
            console.log('Lugar: ' + (resultado.lugar || '-'));
            console.log('Fecha: ' + (resultado.fecha || '-'));
            console.log('Monto: Bs. ' + (resultado.monto ? resultado.monto.toFixed(2) : '-'));
            console.log('Estado: ' + (resultado.estado || '-'));
            console.log('Mensaje: ' + resultado.mensaje);

            if (!resultado.habilitadaParaPago) {
                console.log('No se puede procesar el cobro: ' + resultado.mensaje);
                const continuar = await preguntar('\n¿Desea procesar otra transacción? (s/n): ');
                if (continuar.toLowerCase() !== 's') break;
                continue;
            }

            const confirmar = await preguntar(
                `\nBoleta habilitada. ¿Confirmar cobro de Bs. ${resultado.monto.toFixed(2)}? (s/n): `
            );

            if (confirmar.toLowerCase() !== 's') {
                console.log('Cobro cancelado.');
                const continuar = await preguntar('\n¿Desea procesar otra transacción? (s/n): ');
                if (continuar.toLowerCase() !== 's') break;
                continue;
            }

            const respuestaSocket = await registrarPago(codigo.trim());

            if (respuestaSocket.startsWith('OK')) {
                console.log('\nPAGO REGISTRADO EXITOSAMENTE');
                console.log(respuestaSocket);
            } else {
                console.log('\nError en el pago: ' + respuestaSocket);
            }

        } catch (error) {
            console.error('\nError en la operación: ' + error.message);
        }

        const continuar = await preguntar('\n¿Desea procesar otra transacción? (s/n): ');
        if (continuar.toLowerCase() !== 's') break;
    }

    rl.close();
}

main();
