{{-- resources/views/candidatos/show.blade.php --}}
<x-layouts.app :title="$candidato->nombre_completo">
    <div class="mx-auto w-full max-w-3xl space-y-6">
        <flux:button :href="route('candidatos.index')" icon="arrow-left" variant="ghost" size="sm">
            Volver al listado
        </flux:button>
        
        <div class="rounded-xl border border-zinc-200 p-6 dark:border-zinc-700">
            <div class="flex flex-wrap items-center gap-4">
                <div class="flex size-16 items-center justify-center rounded-full bg-zinc-100 text-xl font-semibold dark:bg-zinc-700">
                    {{ mb_substr($candidato->nombres, 0, 1) }}{{ mb_substr($candidato->apellidos, 0, 1) }}
                </div>
                <div class="flex-1">
                    <flux:heading size="xl" level="1">{{ $candidato->nombre_completo }}</flux:heading>
                    <flux:badge :color="$candidato->colorCargo()" size="sm" class="mt-1">{{ $candidato->cargo }}</flux:badge>
                </div>
            </div>
            
            <flux:separator class="my-6" />
            
            <dl class="grid gap-4 sm:grid-cols-2">
                <div>
                    <dt class="text-sm text-zinc-500">Nombres</dt>
                    <dd class="font-medium">{{ $candidato->nombres }}</dd>
                </div>
                <div>
                    <dt class="text-sm text-zinc-500">Apellidos</dt>
                    <dd class="font-medium">{{ $candidato->apellidos }}</dd>
                </div>
                <div>
                    <dt class="text-sm text-zinc-500">Cargo</dt>
                    <dd class="font-medium">{{ $candidato->cargo }}</dd>
                </div>
                <div>
                    <dt class="text-sm text-zinc-500">Electores</dt>
                    <dd class="font-medium">{{ $candidato->electores }}</dd>
                </div>
                <div>
                    <dt class="text-sm text-zinc-500">Registrado</dt>
                    <dd class="font-medium">{{ $candidato->created_at?->format('d/m/Y H:i') }}</dd>
                </div>
                <div>
                    <dt class="text-sm text-zinc-500">Última modificación</dt>
                    <dd class="font-medium">{{ $candidato->updated_at?->diffForHumans() }}</dd>
                </div>
            </dl>
            
            <div class="mt-6 flex justify-end">
                <flux:button :href="route('candidatos.edit', $candidato)" icon="pencil-square" variant="primary">
                    Editar
                </flux:button>
            </div>
        </div>
    </div>
</x-layouts.app>