<?php

use App\Models\Candidato;
use App\Models\User;

beforeEach(function () {
    $this->actingAs(User::factory()->create());
});

test('invitados son redirigidos al login', function () {
    auth()->logout();
    
    $this->get(route('candidatos.index'))->assertRedirect('/login');
});

test('muestra el listado de candidatos', function () {
    $candidato = Candidato::factory()->create();
    
    $this->get(route('candidatos.index'))
        ->assertOk()
        ->assertSee($candidato->nombre_completo);
});

test('filtra candidatos por nombre y cargo', function () {
    Candidato::factory()->create(['nombres' => 'Ana', 'cargo' => 'Rector']);
    Candidato::factory()->create(['nombres' => 'Luis', 'cargo' => 'Decano']);

    $this->get(route('candidatos.index', ['buscar' => 'Ana']))
        ->assertSee('Ana')->assertDontSee('Luis');

    $this->get(route('candidatos.index', ['cargo' => 'Decano']))
        ->assertSee('Luis')->assertDontSee('Ana');
});

test('muestra los formularios de crear, ver y editar', function () {
    $candidato = Candidato::factory()->create();
    
    $this->get(route('candidatos.create'))->assertOk();
    $this->get(route('candidatos.show', $candidato))->assertOk()->assertSee($candidato->nombres);
    $this->get(route('candidatos.edit', $candidato))->assertOk()->assertSee($candidato->nombres);
});

test('registra un candidato', function () {
    $datos = [
        'nombres' => 'Carlos',
        'apellidos' => 'Montellano',
        'cargo' => 'Rector',
        'electores' => 'Docentes',
    ];

    $this->post(route('candidatos.store'), $datos)
        ->assertRedirect(route('candidatos.index'))
        ->assertSessionHas('success');

    $this->assertDatabaseHas('candidatos', $datos);
});

test('valida los datos del candidato', function () {
    $this->post(route('candidatos.store'), [
        'nombres' => '',
        'apellidos' => str_repeat('a', 51),
        'cargo' => 'Presidente',
        'electores' => '',
    ])->assertSessionHasErrors(['nombres', 'apellidos', 'cargo', 'electores']);

    $this->assertDatabaseCount('candidatos', 0);
});

test('actualiza un candidato', function () {
    $candidato = Candidato::factory()->create();
    
    $this->put(route('candidatos.update', $candidato), [
        'nombres' => 'Nuevo',
        'apellidos' => 'Nombre',
        'cargo' => 'Decano',
        'electores' => 'Estudiantes',
    ])->assertRedirect(route('candidatos.index'));

    expect($candidato->fresh()->nombre_completo)->toBe('Nuevo Nombre');
});

test('elimina un candidato', function () {
    $candidato = Candidato::factory()->create();
    
    $this->delete(route('candidatos.destroy', $candidato))
        ->assertRedirect(route('candidatos.index'));

    $this->assertModelMissing($candidato);
});