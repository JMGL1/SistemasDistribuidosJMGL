{{-- resources/views/candidatos/_form.blade.php --}}
{{-- Formulario compartido por create y edit. old() recupera lo escrito si falla la validación. --}}
<div class="grid gap-6 md:grid-cols-2">
    <flux:input name="nombres" label="Nombres" placeholder="Ej. Juan Carlos"
        :value="old('nombres', $candidato->nombres)" maxlength="50" required />
    <flux:input name="apellidos" label="Apellidos" placeholder="Ej. Pérez Mamani"
        :value="old('apellidos', $candidato->apellidos)" maxlength="50" required />
    
    <flux:select name="cargo" label="Cargo" placeholder="Seleccione un cargo..." required>
        @foreach (\App\Models\Candidato::CARGOS as $opcion)
            <flux:select.option :value="$opcion" :selected="old('cargo', $candidato->cargo) === $opcion">
                {{ $opcion }}
            </flux:select.option>
        @endforeach
    </flux:select>
    
    <flux:select name="electores" label="Electores" placeholder="Seleccione electores..." required>
        @foreach (\App\Models\Candidato::ELECTORES as $opcion)
            <flux:select.option :value="$opcion" :selected="old('electores', $candidato->electores) === $opcion">
                {{ $opcion }}
            </flux:select.option>
        @endforeach
    </flux:select>
</div>