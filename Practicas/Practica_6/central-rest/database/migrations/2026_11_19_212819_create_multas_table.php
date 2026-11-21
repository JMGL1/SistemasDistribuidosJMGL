<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('multas', function (Blueprint $table) {
            $table->id();
            $table->string('codigo', 30)->unique();
            $table->string('ci_conductor', 20);
            $table->string('nombre_conductor', 200);
            $table->string('tipo_infraccion', 150);
            $table->string('lugar', 250);
            $table->decimal('monto', 10, 2);
            $table->date('fecha');
            $table->enum('estado', ['pendiente', 'pagada', 'anulada'])->default('pendiente');
            $table->dateTime('fecha_pago')->nullable();
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('multas');
    }
};
