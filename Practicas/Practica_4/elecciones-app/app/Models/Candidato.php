<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Candidato extends Model
{
    /** @use HasFactory<\Database\Factories\CandidatoFactory> */
    use HasFactory;

    // Opciones válidas: se usan en el factory, la validación y los <select> de las vistas
    public const CARGOS = ['Rector', 'Vicerector', 'Decano', 'Director Carrera'];
    public const ELECTORES = ['Estudiantes', 'Docentes', 'Administrativos'];

    protected $fillable = [
        'nombres',
        'apellidos',
        'cargo',
        'electores',
    ];

    // Atributo calculado: $candidato->nombre_completo
    public function getNombreCompletoAttribute(): string
    {
        return "{$this->nombres} {$this->apellidos}";
    }

    // Color del badge según el cargo (usado en las vistas)
    public function colorCargo(): string
    {
        return match ($this->cargo) {
            'Rector' => 'indigo',
            'Vicerector' => 'sky',
            'Decano' => 'emerald',
            default => 'amber',
        };
    }
}