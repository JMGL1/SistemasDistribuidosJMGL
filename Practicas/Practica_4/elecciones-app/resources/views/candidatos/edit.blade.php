{{-- resources/views/candidatos/edit.blade.php --}}
<x-layouts.app :title="__('Editar candidato')">
    <div class="mx-auto w-full max-w-3xl space-y-6">
        <div>
            <flux:button :href="route('candidatos.index')" icon="arrow-left" variant="ghost" size="sm">
                Volver al listado
            </flux:button>
            <flux:heading size="xl" level="1" class="mt-2">Editar candidato</flux:heading>
            <flux:subheading>Modificando a <strong>{{ $candidato->nombre_completo }}</strong>.</flux:subheading>
        </div>
        
        <form method="POST" action="{{ route('candidatos.update', $candidato) }}"
            class="space-y-6 rounded-xl border border-zinc-200 p-6 dark:border-zinc-700">
            @csrf
            @method('PUT') {{-- HTML solo envía GET/POST; Laravel lee este campo oculto --}}
            @include('candidatos._form')
            
            <div class="flex justify-end gap-2">
                <flux:button :href="route('candidatos.index')" variant="ghost">Cancelar</flux:button>
                <flux:button type="submit" variant="primary" icon="check">Actualizar</flux:button>
            </div>
        </form>
    </div>
</x-layouts.app>