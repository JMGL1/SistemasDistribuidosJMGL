#!/usr/bin/env node

/**
 * ============================================================================
 * EJEMPLO CONTEXTUALIZADO: CONSUMIDOR DE ALUMNOS (PORTAL DEL ESTUDIANTE)
 * Archivo: ejemplo_practico/consumidor_alumnos.js
 * ============================================================================
 *
 * ROL:
 * El portal del estudiante se suscribe a dos tipos de avisos:
 * 1. 'matricula': Asuntos académicos relevantes (fechas de inscripción, notas).
 * 2. 'urgente': Avisos de emergencia o suspensión de clases.
 *
 * Ignora por completo los eventos de 'pagos' internos administrativos.
 */

const amqp = require('amqplib');

async function main() {
  try {
    const connection = await amqp.connect('amqp://localhost');
    const channel = await connection.createChannel();

    const exchange = 'campus_notificaciones';
    await channel.assertExchange(exchange, 'direct', { durable: false });

    // Cola exclusiva para la aplicación móvil / portal del estudiante
    const q = await channel.assertQueue('', { exclusive: true });

    // Definimos las claves que interesan a los estudiantes
    const categoriasInteres = ['matricula', 'urgente'];

    console.log('=================================================================');
    console.log(' [🎓 SERVICIO APP ESTUDIANTES] Iniciado.');
    console.log(' Suscribiendo a categorías: [matricula, urgente]');
    console.log(' Esperando notificaciones para alumnos... (CTRL+C para salir)');
    console.log('=================================================================');

    // Múltiples bindings a la misma cola
    for (const cat of categoriasInteres) {
      await channel.bindQueue(q.queue, exchange, cat);
      console.log(`  -> Enlace activo para: '${cat}'`);
    }

    channel.consume(
      q.queue,
      (msg) => {
        if (msg) {
          const data = JSON.parse(msg.content.toString());
          console.log(`\n🔔 [NOTIFICACIÓN ALUMNO] Tipo: [${msg.fields.routingKey.toUpperCase()}]`);
          console.log(`   Mensaje: "${data.mensaje}"`);
          console.log(`   Origen:  ${data.origen} | Hora: ${data.timestamp}`);
        }
      },
      { noAck: true }
    );

  } catch (error) {
    console.error(' [!] Error en servicio de estudiantes:', error);
  }
}

main();
