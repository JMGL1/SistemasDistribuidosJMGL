<?php

namespace App\Http\Controllers;

use App\Models\Multa;
use Illuminate\Http\Request;

class MultaController extends Controller
{
    public function registrar(Request $request)
    {
        $validacion = $request->validate([
            'codigo'           => 'required|string|max:30|unique:multas,codigo',
            'ci_conductor'     => 'required|string|max:20',
            'nombre_conductor' => 'required|string|max:200',
            'tipo_infraccion'  => 'required|string|max:150',
            'lugar'            => 'required|string|max:250',
            'monto'            => 'required|numeric|min:0.01',
            'fecha'            => 'required|date',
        ]);

        $multa = Multa::create([
            'codigo'           => $validacion['codigo'],
            'ci_conductor'     => $validacion['ci_conductor'],
            'nombre_conductor' => $validacion['nombre_conductor'],
            'tipo_infraccion'  => $validacion['tipo_infraccion'],
            'lugar'            => $validacion['lugar'],
            'monto'            => $validacion['monto'],
            'fecha'            => $validacion['fecha'],
            'estado'           => 'pendiente',
        ]);

        return response()->json([
            'mensaje' => 'Multa registrada exitosamente.',
            'codigo'  => $multa->codigo,
            'estado'  => $multa->estado,
        ], 201);
    }

    public function consultar($codigo)
    {
        $multa = Multa::where('codigo', $codigo)->first();

        if (!$multa) {
            return response()->json([
                'error' => "No se encontró ninguna multa con el código: {$codigo}"
            ], 404);
        }

        return response()->json([
            'codigo'           => $multa->codigo,
            'ci_conductor'     => $multa->ci_conductor,
            'nombre_conductor' => $multa->nombre_conductor,
            'tipo_infraccion'  => $multa->tipo_infraccion,
            'lugar'            => $multa->lugar,
            'monto'            => $multa->monto,
            'fecha'            => $multa->fecha,
            'estado'           => $multa->estado,
            'fecha_pago'       => $multa->fecha_pago,
        ], 200);
    }

    public function listar()
    {
        $multas = Multa::orderBy('created_at', 'desc')->get();

        return response()->json([
            'total'  => $multas->count(),
            'multas' => $multas,
        ], 200);
    }
}
