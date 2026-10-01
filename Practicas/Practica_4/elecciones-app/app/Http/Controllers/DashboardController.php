<?php

namespace App\Http\Controllers;

use App\Models\Candidato;
use Illuminate\Http\Request;

class DashboardController extends Controller
{
    /**
     * Controlador de una sola acción: se ejecuta __invoke().
     */
    public function __invoke(Request $request)
    {
        // Cantidad de candidatos agrupados: ['Rector' => 3, 'Decano' => 5, ...]
        $porCargo = Candidato::selectRaw('cargo, count(*) as total')
            ->groupBy('cargo')
            ->pluck('total', 'cargo');

        $porElectores = Candidato::selectRaw('electores, count(*) as total')
            ->groupBy('electores')
            ->pluck('total', 'electores');

        return view('dashboard', [
            'total' => Candidato::count(),
            'porCargo' => $porCargo,
            'porElectores' => $porElectores,
            'ultimos' => Candidato::latest()->take(5)->get(),
        ]);
    }
}