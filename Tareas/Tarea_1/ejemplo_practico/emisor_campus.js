#!/usr/bin/env node

/**
 * ============================================================================
 * EJEMPLO CONTEXTUALIZADO: SISTEMA DE NOTIFICACIONES UNIVERSITARIAS
 * Archivo: ejemplo_practico/emisor_campus.js
 * ============================================================================
 *
 * CONTEXTO:
 * En una universidad, ocurren eventos de diferente naturaleza:
 * - 'matricula': Avisos académicos dirigidos a alumnos y docentes.
 * - 'pagos': Comprobantes y recordatorios para el área financiera / caja.
 * - 'urgente': Avisos de evacuación o suspensión de clases que deben llegar a todos.
 *
 * En lugar de enviar todo a todos los servicios (como haría un 'fanout'),
 * usamos un Exchange 'direct' para enrutar el mensaje solo a quienes
 * realmente necesitan procesar esa categoría.
 */

const amqp = require('amqplib');

async function main() {
  let connection;
  try {
    connection = await amqp.connect('amqp://localhost');
    const channel = await connection.createChannel();

    const exchange = 'campus_notificaciones';

    // Declaramos un exchange 'direct' para las alertas del campus
    await channel.assertExchange(exchange, 'direct', { durable: false });

    // Leemos la categoría y el texto del evento
    // Uso: node emisor_campus.js <categoria> <mensaje>
    // Categorías válidas de ejemplo: 'matricula', 'pagos', 'urgente'
    const args = process.argv.slice(2);
    const categoria = args[0] || 'matricula';
    const mensaje = args.slice(1).join(' ') || 'Notificación general del sistema universitario.';

    // Creamos un payload JSON con información contextual
    const evento = {
      id: Math.floor(1000 + Math.random() * 9000),
      timestamp: new Date().toISOString(),
      categoria: categoria,
      mensaje: mensaje,
      origen: 'Portal Central del Campus'
    };

    const payload = JSON.stringify(evento, null, 2);

    // Publicamos con la clave de enrutamiento igual a la categoría
    channel.publish(exchange, categoria, Buffer.from(payload));

    console.log('====================================================');
    console.log(` [📢 EMISOR CAMPUS] Evento publicado con éxito:`);
    console.log(`   -> Exchange:    '${exchange}'`);
    console.log(`   -> Routing Key: '${categoria}'`);
    console.log(`   -> ID Evento:   #${evento.id}`);
    console.log(`   -> Detalle:     "${mensaje}"`);
    console.log('====================================================');

  } catch (error) {
    console.error(' [!] Error al emitir la notificación universitaria:', error);
  } finally {
    setTimeout(async () => {
      if (connection) await connection.close();
      process.exit(0);
    }, 500);
  }
}

main();
