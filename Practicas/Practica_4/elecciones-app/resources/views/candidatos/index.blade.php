{{-- resources/views/candidatos/index.blade.php --}}
<x-layouts.app :title="__('Candidatos')">
    <div class="flex w-full flex-col gap-6">
        {{-- Encabezado --}}
        <div class="flex flex-wrap items-end justify-between gap-4">
            <div>
                <flux:heading size="xl" level="1">Candidatos</flux:heading>
                <flux:subheading>Gestiona los candidatos del proceso electoral.</flux:subheading>
            </div>
            <flux:button :href="route('candidatos.create')" variant="primary" icon="plus">
                Nuevo candidato
            </flux:button>
        </div>

        {{-- Mensaje flash enviado desde el controlador con ->with('success', ...) --}}
        @session('success')
            <flux:callout variant="success" icon="check-circle" :heading="$value" />
        @endsession

        {{-- Filtros: formulario GET, los valores viajan en la URL (?buscar=...&cargo=...) --}}
        <form method="GET" action="{{ route('candidatos.index') }}" class="flex flex-wrap items-end gap-3">
            <div class="min-w-60 flex-1">
                <flux:input name="buscar" :value="$buscar" icon="magnifying-glass"
                    placeholder="Buscar por nombre o apellido..." />
            </div>
            <div class="w-52">
                <flux:select name="cargo">
                    <flux:select.option value="">Todos los cargos</flux:select.option>
                    @foreach (\App\Models\Candidato::CARGOS as $opcion)
                        <flux:select.option :value="$opcion" :selected="$cargo === $opcion">{{ $opcion }}</flux:select.option>
                    @endforeach
                </flux:select>
            </div>
            <flux:button type="submit" icon="funnel">Filtrar</flux:button>
            @if ($buscar || $cargo)
                <flux:button :href="route('candidatos.index')" variant="ghost" icon="x-mark">Limpiar</flux:button>
            @endif
        </form>

        {{-- Tabla --}}
        <div class="rounded-xl border border-zinc-200 px-4 dark:border-zinc-700">
            <flux:table>
                <flux:table.columns>
                    <flux:table.column>#</flux:table.column>
                    <flux:table.column>Candidato</flux:table.column>
                    <flux:table.column>Cargo</flux:table.column>
                    <flux:table.column>Electores</flux:table.column>
                    <flux:table.column align="end">Acciones</flux:table.column>
                </flux:table.columns>
                <flux:table.rows>
                    @forelse ($candidatos as $candidato)
                        <flux:table.row>
                            <flux:table.cell>{{ $candidato->id }}</flux:table.cell>
                            <flux:table.cell variant="strong">{{ $candidato->nombre_completo }}</flux:table.cell>
                            <flux:table.cell>
                                <flux:badge :color="$candidato->colorCargo()" size="sm" inset="top bottom">
                                    {{ $candidato->cargo }}
                                </flux:badge>
                            </flux:table.cell>
                            <flux:table.cell>{{ $candidato->electores }}</flux:table.cell>
                            <flux:table.cell align="end">
                                <div class="flex justify-end gap-1">
                                    <flux:button :href="route('candidatos.show', $candidato)" icon="eye" size="sm" variant="ghost" tooltip="Ver" />
                                    <flux:button :href="route('candidatos.edit', $candidato)" icon="pencil-square" size="sm" variant="ghost" tooltip="Editar" />
                                    <flux:modal.trigger :name="'eliminar-'.$candidato->id">
                                        <flux:button icon="trash" size="sm" variant="ghost" tooltip="Eliminar" class="text-red-500!" />
                                    </flux:modal.trigger>
                                </div>

                                {{-- Modal de confirmación: el DELETE se envía con un form + @method --}}
                                <flux:modal :name="'eliminar-'.$candidato->id" class="min-w-88">
                                    <form method="POST" action="{{ route('candidatos.destroy', $candidato) }}" class="space-y-6 text-left whitespace-normal">
                                        @csrf
                                        @method('DELETE')
                                        <div>
                                            <flux:heading size="lg">¿Eliminar candidato?</flux:heading>
                                            <flux:text class="mt-2">
                                                Se eliminará a <strong>{{ $candidato->nombre_completo }}</strong>.
                                                Esta acción no se puede deshacer.
                                            </flux:text>
                                        </div>
                                        <div class="flex justify-end gap-2">
                                            <flux:modal.close>
                                                <flux:button variant="ghost">Cancelar</flux:button>
                                            </flux:modal.close>
                                            <flux:button type="submit" variant="danger" icon="trash">Eliminar</flux:button>
                                        </div>
                                    </form>
                                </flux:modal>

                            </flux:table.cell>
                        </flux:table.row>
                    @empty
                        <flux:table.row>
                            <flux:table.cell colspan="5" class="py-10 text-center text-zinc-500">
                                No se encontraron candidatos.
                            </flux:table.cell>
                        </flux:table.row>
                    @endforelse
                </flux:table.rows>
            </flux:table>
        </div>

        {{-- Paginación de Laravel (conserva los filtros gracias a withQueryString) --}}
        {{ $candidatos->links() }}
    </div>
</x-layouts.app>