<?php

namespace App\Http\Controllers;

use App\Http\Requests\CandidatoRequest;
use App\Models\Candidato;
use Illuminate\Http\Request;

class CandidatoController extends Controller
{
    /**
     * Display a listing of the resource.
     * GET /candidatos
     */
    public function index(Request $request)
    {
        $buscar = $request->input('buscar');
        $cargo = $request->input('cargo');

        $candidatos = Candidato::query()
            ->when($buscar, function ($query, $buscar) {
                $query->where(function ($q) use ($buscar) {
                    $q->where('nombres', 'like', "%{$buscar}%")
                      ->orWhere('apellidos', 'like', "%{$buscar}%");
                });
            })
            ->when($cargo, fn ($query, $cargo) => $query->where('cargo', $cargo))
            ->latest()
            ->paginate(10)
            ->withQueryString(); // conserva ?buscar=...&cargo=... al cambiar de página

        return view('candidatos.index', compact('candidatos', 'buscar', 'cargo'));
    }

    /**
     * Show the form for creating a new resource.
     * GET /candidatos/create
     */
    public function create()
    {
        // Un candidato vacío permite reutilizar el mismo formulario (_form) en crear y editar
        return view('candidatos.create', ['candidato' => new Candidato]);
    }

    /**
     * Store a newly created resource in storage.
     * POST /candidatos
     */
    public function store(CandidatoRequest $request)
    {
        Candidato::create($request->validated());

        return redirect()->route('candidatos.index')
            ->with('success', 'Candidato registrado correctamente.');
    }

    /**
     * Display the specified resource.
     * GET /candidatos/{candidato}
     */
    public function show(Candidato $candidato)
    {
        return view('candidatos.show', compact('candidato'));
    }

    /**
     * Show the form for editing the specified resource.
     * GET /candidatos/{candidato}/edit
     */
    public function edit(Candidato $candidato)
    {
        return view('candidatos.edit', compact('candidato'));
    }

    /**
     * Update the specified resource in storage.
     * PUT /candidatos/{candidato}
     */
    public function update(CandidatoRequest $request, Candidato $candidato)
    {
        $candidato->update($request->validated());

        return redirect()->route('candidatos.index')
            ->with('success', 'Candidato actualizado correctamente.');
    }

    /**
     * Remove the specified resource from storage.
     * DELETE /candidatos/{candidato}
     */
    public function destroy(Candidato $candidato)
    {
        $candidato->delete();

        return redirect()->route('candidatos.index')
            ->with('success', 'Candidato eliminado correctamente.');
    }
}