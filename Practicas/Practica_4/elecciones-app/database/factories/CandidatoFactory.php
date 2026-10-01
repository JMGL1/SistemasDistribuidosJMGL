<?php

namespace Database\Factories;

use App\Models\Candidato;
use Illuminate\Database\Eloquent\Factories\Factory;

/**
 * @extends Factory<Candidato>
 */
class CandidatoFactory extends Factory
{
    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition(): array
    {
        return [
            'nombres' => fake()->firstName(),
            'apellidos' => fake()->lastName(),
            'cargo' => fake()->randomElement(Candidato::CARGOS),
            'electores' => fake()->randomElement(Candidato::ELECTORES),
        ];
    }
}