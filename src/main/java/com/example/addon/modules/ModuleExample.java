package com.example.addon.modules;

import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import net.minecraft.class_1713;
import net.minecraft.class_1657;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import net.minecraft.class_1802;
import net.minecraft.class_1792;
import meteordevelopment.meteorclient.events.world.TickEvent;
import java.util.Iterator;
import net.minecraft.class_1533;
import net.minecraft.class_1297;
import net.minecraft.class_3965;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import meteordevelopment.meteorclient.utils.player.Rotations;
import net.minecraft.class_1268;
import net.minecraft.class_2338;
import meteordevelopment.meteorclient.settings.IntSetting;
import com.example.addon.Addon;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;

public class FrameDupe extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<Integer> ticks;
    private int timer;
    private boolean atacked;
    private class_243 lastPos;
    private class_2350 lastDirection;
    
    public FrameDupe() {
        super(Addon.CATEGORY, "PickleDupe", "Skidded From Colonizadores 🤓");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.ticks = (Setting<Integer>)this.sgGeneral.add((Setting)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)new IntSetting.Builder().name("ticks")).description("Ticks")).defaultValue((Object)6)).range(0, 100).sliderMax(100).build());
        this.timer = 0;
        this.atacked = false;
        this.lastPos = null;
        this.lastDirection = null;
    }
    
    public void placeNearestSide(final class_2338 blockPos, final class_2350 Side) {
        final class_2350 direction = Side;
        if (direction != null) {
            final class_243 blockHit = new class_243(blockPos.method_10263() + 0.5, blockPos.method_10264() + 0.5, blockPos.method_10260() + 0.5);
            final class_2338 neighbor = blockPos.method_10093(direction.method_10153());
            blockHit.method_1031(direction.method_10148() * 0.5, direction.method_10164() * 0.5, direction.method_10165() * 0.5);
            this.mc.field_1724.method_6104(class_1268.field_5808);
            this.mc.method_1562().method_2883((class_2596)new class_2828.class_2831((float)Rotations.getYaw(blockHit), (float)Rotations.getPitch(blockHit), this.mc.field_1724.method_24828()));
            this.mc.field_1761.method_2896(this.mc.field_1724, class_1268.field_5808, new class_3965(blockHit, direction, neighbor, false));
        }
    }
    
    private void place(final class_2338 blockPos) {
        class_2350 nearestSide = null;
        class_2338 nearesBlockPos = null;
        double nearestSideInt = 100.0;
        for (final class_2350 side : class_2350.values()) {
            if (this.mc.field_1724.method_5707(new class_243((double)(blockPos.method_10263() + side.method_10148()), (double)blockPos.method_10264(), (double)(blockPos.method_10260() + side.method_10165()))) < nearestSideInt) {
                nearestSideInt = this.mc.field_1724.method_5707(new class_243((double)(blockPos.method_10263() + side.method_10148()), (double)blockPos.method_10264(), (double)(blockPos.method_10260() + side.method_10165())));
                nearestSide = side;
                nearesBlockPos = blockPos;
            }
        }
        this.placeNearestSide(nearesBlockPos.method_10093(nearestSide), nearestSide);
    }
    
    public class_1297 getEntity() {
        for (final class_1297 entity : this.mc.field_1687.method_18112()) {
            if (this.mc.field_1724.method_5739(entity) < 4.0f && entity instanceof class_1533) {
                return entity;
            }
        }
        return null;
    }
    
    @EventHandler
    private void onTick(final TickEvent.Pre event) {
        ++this.timer;
        final class_1297 frame = this.getEntity();
        if (frame == null && this.lastPos != null && this.timer > 10) {
            final FindItemResult itemframeitem = InvUtils.findInHotbar(new class_1792[] { class_1802.field_8143 });
            if (itemframeitem.found()) {
                this.mc.field_1724.method_31548().field_7545 = itemframeitem.slot();
                this.place(new class_2338(this.lastPos.method_1031((double)this.lastDirection.method_10148(), (double)this.lastDirection.method_10164(), (double)this.lastDirection.method_10165())));
                this.timer = 0;
                this.atacked = true;
            }
        }
        if (frame == null) {
            return;
        }
        this.lastPos = frame.method_19538();
        this.lastDirection = frame.method_5735().method_10153();
        if ((!((class_1533)frame).method_6940().method_7960() && !this.atacked) || (!((class_1533)frame).method_6940().method_7960() && this.timer > (int)this.ticks.get())) {
            this.mc.field_1761.method_2918((class_1657)this.mc.field_1724, frame);
            this.atacked = true;
            this.timer = 0;
        }
        if (((class_1533)frame).method_6940().method_7960()) {
            this.mc.field_1724.method_31548().field_7545 = 0;
            this.mc.field_1761.method_2906(this.mc.field_1724.field_7512.field_7763, 36, 0, class_1713.field_7790, (class_1657)this.mc.field_1724);
            this.mc.field_1724.method_31548().method_7381();
            this.mc.field_1761.method_2905((class_1657)this.mc.field_1724, frame, class_1268.field_5808);
            this.atacked = false;
        }
    }
}
