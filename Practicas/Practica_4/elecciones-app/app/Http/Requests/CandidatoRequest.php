<?php

namespace App\Http\Requests;

use App\Models\Candidato;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

class CandidatoRequest extends FormRequest
{
    /**
     * Determine if the user is authorized to make this request.
     */
    public function authorize(): bool
    {
        return true;
    }

    /**
     * Get the validation rules that apply to the request.
     * Las longitudes coinciden con las columnas de la migración.
     *
     * @return array<string, \Illuminate\Contracts\Validation\ValidationRule|array<mixed>|string>
     */
    public function rules(): array
    {
        return [
            'nombres' => ['required', 'string', 'max:50'],
            'apellidos' => ['required', 'string', 'max:50'],
            'cargo' => ['required', Rule::in(Candidato::CARGOS)],
            'electores' => ['required', Rule::in(Candidato::ELECTORES)],
        ];
    }

    /**
     * Mensajes de error en español.
     */
    public function messages(): array
    {
        return [
            'required' => 'El campo :attribute es obligatorio.',
            'max' => 'El campo :attribute no debe superar :max caracteres.',
            'in' => 'El :attribute seleccionado no es válido.',
        ];
    }
}