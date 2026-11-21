<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Multa extends Model
{
    protected $table = 'multas';

    protected $fillable = [
        'codigo',
        'ci_conductor',
        'nombre_conductor',
        'tipo_infraccion',
        'lugar',
        'monto',
        'fecha',
        'estado',
        'fecha_pago',
    ];

    protected $casts = [
        'fecha'      => 'date:Y-m-d',
        'fecha_pago' => 'datetime',
        'monto'      => 'float',
    ];
}
