{{-- resources/views/candidatos/create.blade.php --}}
<x-layouts.app :title="__('Nuevo candidato')">
    <div class="mx-auto w-full max-w-3xl space-y-6">
        <div>
            <flux:button :href="route('candidatos.index')" icon="arrow-left" variant="ghost" size="sm">
                Volver al listado
            </flux:button>
            <flux:heading size="xl" level="1" class="mt-2">Nuevo candidato</flux:heading>
            <flux:subheading>Registra un candidato para el proceso electoral.</flux:subheading>
        </div>
        
        <form method="POST" action="{{ route('candidatos.store') }}"
            class="space-y-6 rounded-xl border border-zinc-200 p-6 dark:border-zinc-700">
            @csrf
            @include('candidatos._form')
            <div class="flex justify-end gap-2">
                <flux:button :href="route('candidatos.index')" variant="ghost">Cancelar</flux:button>
                <flux:button type="submit" variant="primary" icon="check">Guardar</flux:button>
            </div>
        </form>
    </div>
</x-layouts.app>