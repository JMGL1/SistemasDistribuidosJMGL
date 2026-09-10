#!/usr/bin/env node

/**
 * ============================================================================
 * EJEMPLO CONTEXTUALIZADO: CONSUMIDOR DEL ÁREA FINANCIERA / CAJA
 * Archivo: ejemplo_practico/consumidor_caja.js
 * ============================================================================
 *
 * ROL:
 * El departamento financiero únicamente necesita procesar y registrar
 * las transacciones y recibos bajo la routing key: 'pagos'.
 * No recibe mensajes de matrículas ni avisos generales.
 */

const amqp = require('amqplib');

async function main() {
  try {
    const connection = await amqp.connect('amqp://localhost');
    const channel = await connection.createChannel();

    const exchange = 'campus_notificaciones';
    await channel.assertExchange(exchange, 'direct', { durable: false });

    // Cola exclusiva del módulo de caja
    const q = await channel.assertQueue('', { exclusive: true });

    const bindingKey = 'pagos';
    await channel.bindQueue(q.queue, exchange, bindingKey);

    console.log('=================================================================');
    console.log(' [💳 SERVICIO FINANCIERO Y CAJA] Iniciado.');
    console.log(` Suscrito exclusivamente a la clave: '${bindingKey}'`);
    console.log(' Esperando eventos de facturación y pagos... (CTRL+C para salir)');
    console.log('=================================================================');

    channel.consume(
      q.queue,
      (msg) => {
        if (msg) {
          const data = JSON.parse(msg.content.toString());
          console.log(`\n💰 [REGISTRO CONTABLE RECIBIDO]`);
          console.log(`   ID Comprobante / Transacción: #${data.id}`);
          console.log(`   Concepto: "${data.mensaje}"`);
          console.log(`   Fecha:    ${data.timestamp}`);
        }
      },
      { noAck: true }
    );

  } catch (error) {
    console.error(' [!] Error en servicio financiero:', error);
  }
}

main();
