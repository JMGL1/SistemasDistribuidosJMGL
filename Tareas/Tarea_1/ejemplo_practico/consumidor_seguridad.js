#!/usr/bin/env node

/**
 * ============================================================================
 * EJEMPLO CONTEXTUALIZADO: CONSUMIDOR DE SEGURIDAD Y EMERGENCIAS
 * Archivo: ejemplo_practico/consumidor_seguridad.js
 * ============================================================================
 *
 * ROL:
 * El centro de control, vigilancia y brigada de emergencias del campus
 * solo atiende eventos con la routing key: 'urgente'.
 * Debe responder de inmediato y activar protocolos de seguridad física.
 */

const amqp = require('amqplib');

async function main() {
  try {
    const connection = await amqp.connect('amqp://localhost');
    const channel = await connection.createChannel();

    const exchange = 'campus_notificaciones';
    await channel.assertExchange(exchange, 'direct', { durable: false });

    // Cola exclusiva del centro de monitoreo
    const q = await channel.assertQueue('', { exclusive: true });

    const bindingKey = 'urgente';
    await channel.bindQueue(q.queue, exchange, bindingKey);

    console.log('=================================================================');
    console.log(' [🚨 CENTRO DE SEGURIDAD Y EMERGENCIAS] Iniciado.');
    console.log(` Suscrito en alerta para eventos críticos: '${bindingKey}'`);
    console.log(' Monitoreando en tiempo real... (CTRL+C para salir)');
    console.log('=================================================================');

    channel.consume(
      q.queue,
      (msg) => {
        if (msg) {
          const data = JSON.parse(msg.content.toString());
          console.log('\n🚨🚨🚨 ALERTA CRÍTICA DETECTADA EN EL CAMPUS 🚨🚨🚨');
          console.log(`   ID Incidente: #${data.id}`);
          console.log(`   Detalle:      "${data.mensaje}"`);
          console.log(`   Hora Alerta:  ${data.timestamp}`);
          console.log('   Acción:       Activando brigada de respuesta y sirenas.');
          console.log('----------------------------------------------------');
        }
      },
      { noAck: true }
    );

  } catch (error) {
    console.error(' [!] Error en centro de seguridad:', error);
  }
}

main();
