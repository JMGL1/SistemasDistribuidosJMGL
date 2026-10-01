{{-- resources/views/dashboard.blade.php --}}
<x-layouts.app :title="__('Dashboard')">
    <div class="flex w-full flex-1 flex-col gap-6">
        <div>
            <flux:heading size="xl" level="1">Dashboard</flux:heading>
            <flux:subheading>Resumen del proceso electoral.</flux:subheading>
        </div>

        {{-- Tarjetas de resumen --}}
        <div class="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
            <div class="rounded-xl border border-zinc-200 p-5 dark:border-zinc-700">
                <flux:text>Total candidatos</flux:text>
                <p class="mt-2 text-3xl font-semibold">{{ $total }}</p>
            </div>
            @foreach (\App\Models\Candidato::ELECTORES as $grupo)
                <div class="rounded-xl border border-zinc-200 p-5 dark:border-zinc-700">
                    <flux:text>Electores: {{ $grupo }}</flux:text>
                    <p class="mt-2 text-3xl font-semibold">{{ $porElectores[$grupo] ?? 0 }}</p>
                </div>
            @endforeach
        </div>

        <div class="grid gap-4 lg:grid-cols-2">
            {{-- Distribución por cargo con barras simples de Tailwind --}}
            <div class="rounded-xl border border-zinc-200 p-5 dark:border-zinc-700">
                <flux:heading size="lg">Candidatos por cargo</flux:heading>
                <div class="mt-4 space-y-4">
                    @foreach (\App\Models\Candidato::CARGOS as $cargo)
                        @php($cantidad = $porCargo[$cargo] ?? 0)
                        <div>
                            <div class="mb-1 flex justify-between text-sm">
                                <span>{{ $cargo }}</span>
                                <span class="text-zinc-500">{{ $cantidad }}</span>
                            </div>
                            <div class="h-2 rounded-full bg-zinc-100 dark:bg-zinc-700">
                                <div class="h-2 rounded-full bg-zinc-800 dark:bg-white"
                                    style="width: {{ $total ? round($cantidad * 100 / $total) : 0 }}%"></div>
                            </div>
                        </div>
                    @endforeach
                </div>
            </div>

            {{-- Últimos registrados --}}
            <div class="rounded-xl border border-zinc-200 p-5 dark:border-zinc-700">
                <div class="flex items-center justify-between">
                    <flux:heading size="lg">Últimos registrados</flux:heading>
                    <flux:button :href="route('candidatos.index')" size="sm" variant="ghost" icon-trailing="arrow-right">
                        Ver todos
                    </flux:button>
                </div>
                <ul class="mt-4 divide-y divide-zinc-100 dark:divide-zinc-700">
                    @forelse ($ultimos as $candidato)
                        <li class="flex items-center justify-between py-2">
                            <a href="{{ route('candidatos.show', $candidato) }}" class="font-medium hover:underline">
                                {{ $candidato->nombre_completo }}
                            </a>
                            <flux:badge :color="$candidato->colorCargo()" size="sm">{{ $candidato->cargo }}</flux:badge>
                        </li>
                    @empty
                        <li class="py-2 text-zinc-500">Aún no hay candidatos.</li>
                    @endforelse
                </ul>
            </div>
        </div>
    </div>
</x-layouts.app>