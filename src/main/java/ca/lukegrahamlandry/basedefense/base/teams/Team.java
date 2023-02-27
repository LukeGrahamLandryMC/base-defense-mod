package ca.lukegrahamlandry.basedefense.base.teams;

import ca.lukegrahamlandry.basedefense.base.attacks.OngoingAttack;
import ca.lukegrahamlandry.basedefense.base.attacks.AttackLocation;
import ca.lukegrahamlandry.basedefense.base.material.MaterialCollection;
import ca.lukegrahamlandry.basedefense.base.material.MaterialGeneratorType;
import ca.lukegrahamlandry.basedefense.events.ServerEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Team {
    UUID id;
    MaterialCollection materials;
    Map<UUID, MaterialGeneratorType.Instance> generators;
    public OngoingAttack.State attack = null;
    Set<AttackLocation> attackLocations;
    int baseBlockTier = 0;
    UUID owner;

    public Team() {
        this.id = UUID.randomUUID();
        this.materials = new MaterialCollection();
        this.generators = new HashMap<>();
        this.attackLocations = new HashSet<>();
    }

    public boolean contains(Player player) {
        return TeamManager.get(player).id.equals(this.id);
    }

    public MaterialCollection getMaterials() {
        return this.materials;
    }

    public Map<UUID, MaterialGeneratorType.Instance> getGenerators(){
        return generators;
    }

    public void addAttackLocation(AttackLocation attackLocation) {
         this.attackLocations.add(attackLocation);
    }

    public void removeAttackLocation(AttackLocation attackLocation) {
        this.attackLocations.remove(attackLocation);
    }


    public List<AttackLocation> getAttackOptions() {
        return new ArrayList<>(this.attackLocations);
    }

    public UUID getId() {
        return this.id;
    }

    public void addGenerator(UUID uuid, MaterialGeneratorType.Instance instance) {
        generators.put(uuid, instance);
        TeamManager.TEAMS.setDirty();
    }

    public void removeGenerator(UUID uuid) {
        generators.remove(uuid);
        TeamManager.TEAMS.setDirty();
    }

    public int getBaseTier() {
        return this.baseBlockTier;
    }

    public void setDirty(){
        TeamManager.TEAMS.setDirty();
    }

    public void upgradeBaseTier() {
        this.baseBlockTier++;
    }

    public void message(Component text) {
        text.getStyle().applyFormat(ChatFormatting.LIGHT_PURPLE);
        ServerEvents.server.getPlayerList().getPlayers().forEach((player -> {
            if (contains(player)) player.displayClientMessage(text, false);
        }));
    }

    public Collection<UUID> getMembers(){
        return TeamManager.PLAYER_TEAMS.getMap().entrySet().stream().filter((entry) -> entry.getValue().id.equals(this.getId())).map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "Team:" + this.id;
    }

    public UUID getOwner() {
        var members = getMembers();
        if (this.owner == null || !members.contains(this.owner)){
            if (!members.isEmpty()){
                this.owner = members.stream().findFirst().get();
            }
        }
        return this.owner;
    }
}
