package dev.myclxss.listener;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import dev.myclxss.API;

public class CombatListener implements Listener {

    private Map<Player, Player> jugadoresEnCombate = new HashMap<>();

    private Map<Player, Integer> temporizadoresCombate = new HashMap<>();
    private Map<Player, Integer> temporizadoresTag = new HashMap<>();

    private int tiempoInactividadCombate = 60; // 10 segundos de inactividad en combate antes de finalizar
    private int tiempoTag = 60; // 10 segundos de "tag" antes de eliminarlo

    // if (API.getInstance().getArenaUsers().contains(player.getUniqueId())) {

    @EventHandler
    public void onUserCombat(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player jugadorAtacante = (Player) event.getDamager();
            Player jugadorDefensor = (Player) event.getEntity();

            if (API.getInstance().getArenaUsers().contains(jugadorAtacante.getUniqueId()) &&
                    API.getInstance().getArenaUsers().contains(jugadorDefensor.getUniqueId())) {

                // Verificar si los dos jugadores están en combate
                if (!estaEnCombate(jugadorAtacante) && !estaEnCombate(jugadorDefensor)) {
                    // Si ninguno de los dos jugadores está en combate, inicia el combate
                    iniciarCombate(jugadorAtacante, jugadorDefensor);
                }

                // Reiniciar el temporizador de inactividad en combate para ambos jugadores
                reiniciarTemporizadorCombate(jugadorAtacante);
                reiniciarTemporizadorCombate(jugadorDefensor);

                // Reiniciar el temporizador de "tag" para ambos jugadores
                reiniciarTemporizadorTag(jugadorAtacante);
                reiniciarTemporizadorTag(jugadorDefensor);

            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player jugadorDesconectado = event.getPlayer();

        if (API.getInstance().getArenaUsers().contains(jugadorDesconectado.getUniqueId())) {

            if (jugadoresEnCombate.containsKey(jugadorDesconectado)) {
                Player jugadorAtacante = jugadoresEnCombate.get(jugadorDesconectado);
                jugadoresEnCombate.remove(jugadorDesconectado);
                jugadoresEnCombate.remove(jugadorAtacante);
                jugadorDesconectado.setHealth(0.0); // Hacer que el jugador muera instantáneamente
                Bukkit.broadcastMessage(jugadorAtacante.getName() + " ha derrotado a " + jugadorDesconectado.getName()+ " en combate.");
            }
            if (API.getInstance().getArenaUsers().contains(jugadorDesconectado.getUniqueId())) {
                API.getInstance().getArenaUsers().remove(jugadorDesconectado.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onDead(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (API.getInstance().getArenaUsers().contains(player.getUniqueId())) {
            API.getInstance().getArenaUsers().remove(player.getUniqueId());
            player.sendMessage(ChatColor.YELLOW + "Moriste estando dentro de laa lista, y fuiste removido");
        }

    }
    // Resto del código como se mencionó en respuestas anteriores...

    // Implementa tu propio sistema para rastrear si los jugadores están en combate
    private boolean estaEnCombate(Player player) {
        return jugadoresEnCombate.containsKey(player);
    }

    // Este método inicia el combate entre dos jugadores
    public void iniciarCombate(Player jugador1, Player jugador2) {
        jugadoresEnCombate.put(jugador1, jugador2);
        jugadoresEnCombate.put(jugador2, jugador1);
        jugador1.sendMessage("¡Has entrado en combate contra " + jugador2.getName() + "!");
        jugador2.sendMessage("¡Has entrado en combate contra " + jugador1.getName() + "!");
    }

    // Este método finaliza el combate entre dos jugadores
    public void finalizarCombate(Player jugador1, Player jugador2) {
        jugadoresEnCombate.remove(jugador1);
        jugadoresEnCombate.remove(jugador2);
        jugador1.sendMessage("¡El combate contra " + jugador2.getName() + " ha terminado!");
        jugador2.sendMessage("¡El combate contra " + jugador1.getName() + " ha terminado!");
    }

    // Este método reinicia el temporizador de inactividad en combate para un
    // jugador
    public void reiniciarTemporizadorCombate(Player player) {
        temporizadoresCombate.put(player, 0);

        // Programar una tarea para verificar la inactividad en combate
        new BukkitRunnable() {
            @Override
            public void run() {
                if (temporizadoresCombate.containsKey(player)) {
                    int tiempoTranscurrido = temporizadoresCombate.get(player);
                    tiempoTranscurrido++;

                    if (tiempoTranscurrido >= tiempoInactividadCombate) {
                        // Si ha pasado el tiempo de inactividad en combate, finaliza el combate
                        jugadorEnInactividadCombate(player);
                    } else {
                        // Si no ha pasado el tiempo de inactividad en combate, actualiza el
                        // temporizador
                        temporizadoresCombate.put(player, tiempoTranscurrido);
                    }
                }
            }
        }.runTaskTimer(API.getInstance().getMain(), 20L, 20L); // Ejecutar cada segundo (20 ticks)
    }

    // Este método se llama cuando un jugador está inactivo en combate durante el
    // tiempo especificado
    public void jugadorEnInactividadCombate(Player player) {
        if (jugadoresEnCombate.containsKey(player)) {
            Player jugadorEnemigo = jugadoresEnCombate.get(player);
            finalizarCombate(player, jugadorEnemigo);
        }
        temporizadoresCombate.remove(player);
    }

    // Este método reinicia el temporizador de "tag" para un jugador
    public void reiniciarTemporizadorTag(Player player) {
        temporizadoresTag.put(player, 0);

        // Programar una tarea para verificar la inactividad en "tag"
        new BukkitRunnable() {
            @Override
            public void run() {
                if (temporizadoresTag.containsKey(player)) {
                    int tiempoTranscurrido = temporizadoresTag.get(player);
                    tiempoTranscurrido++;

                    if (tiempoTranscurrido >= tiempoTag) {
                        // Si ha pasado el tiempo de "tag", eliminar el "tag"
                        jugadorEnInactividadTag(player);
                    } else {
                        // Si no ha pasado el tiempo de "tag", actualiza el temporizador
                        temporizadoresTag.put(player, tiempoTranscurrido);
                    }
                }
            }
        }.runTaskTimer(API.getInstance().getMain(), 20L, 20L); // Ejecutar cada segundo (20 ticks)
    }

    // Este método se llama cuando un jugador está inactivo en "tag" durante el
    // tiempo especificado
    public void jugadorEnInactividadTag(Player player) {
        temporizadoresTag.remove(player);
    }
}