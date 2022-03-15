package de.crafty.jukeboxmc.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import de.crafty.jukeboxmc.JukeboxMC;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.*;
import org.jetbrains.annotations.NotNull;

public class SettingsScreen extends Screen {

    private final Screen lastScreen;
    private final JukeboxMC jukeboxMC;
    private EditBox inGamePrefixEdit, jukeboxPrefixEdit, keyEdit, jukeboxURLEdit;

    public SettingsScreen(Screen lastScreen) {
        super(new TranslatableComponent("jukeboxmc.screen.title"));
        this.lastScreen = lastScreen;
        this.jukeboxMC = JukeboxMC.getInstance();
    }

    @Override
    protected void init() {

        this.inGamePrefixEdit = new EditBox(this.font, this.width / 2 - 25 - 100, this.height / 6, 50, 20, this.inGamePrefixEdit, new TranslatableComponent("jukeboxmc.enterIngamePrefix"));
        this.inGamePrefixEdit.setValue(jukeboxMC.inGamePrefix);
        this.addWidget(this.inGamePrefixEdit);

        this.jukeboxPrefixEdit = new EditBox(this.font, this.width / 2 - 25 - 100, this.height / 6 + 24, 50, 20, this.jukeboxPrefixEdit, new TranslatableComponent("jukeboxmc.enterJukeboxPrefix"));
        this.jukeboxPrefixEdit.setValue(jukeboxMC.jukeboxPrefix);
        this.addWidget(this.jukeboxPrefixEdit);

        this.keyEdit = new EditBox(this.font, this.width / 2 - 25 - 100, this.height / 6 + 48, 200, 20, this.keyEdit, new TranslatableComponent("jukeboxmc.enterKey"));
        this.keyEdit.setMaxLength(200);
        this.keyEdit.setValue(jukeboxMC.key);
        this.addWidget(this.keyEdit);

        this.jukeboxURLEdit = new EditBox(this.font, this.width / 2 - 25 - 100, this.height / 6 + 72, 200, 20, this.jukeboxURLEdit, new TranslatableComponent("jukeboxmc.enterJukeboxURL"));
        this.jukeboxURLEdit.setMaxLength(200);
        this.jukeboxURLEdit.setValue(jukeboxMC.jukeboxURL);
        this.addWidget(this.jukeboxURLEdit);

        this.addRenderableWidget(new Button(this.width / 2 - 25 - 100 - 54, this.height / 6, 50, 20, new TranslatableComponent("jukeboxmc.screen.reset"), (p_96257_) -> {
            jukeboxMC.inGamePrefix = ".";
            this.inGamePrefixEdit.setValue(jukeboxMC.inGamePrefix);
        }));
        this.addRenderableWidget(new Button(this.width / 2 - 25 - 100 - 54, this.height / 6 + 24, 50, 20, new TranslatableComponent("jukeboxmc.screen.reset"), (p_96257_) -> {
            jukeboxMC.jukeboxPrefix = "§";
            this.jukeboxPrefixEdit.setValue(jukeboxMC.jukeboxPrefix);
        }));
        this.addRenderableWidget(new Button(this.width / 2 - 25 - 100 - 54, this.height / 6 + 48, 50, 20, new TranslatableComponent("jukeboxmc.screen.reset"), (p_96257_) -> {
            jukeboxMC.key = "Unknown";
            this.keyEdit.setValue(jukeboxMC.key);
        }));
        this.addRenderableWidget(new Button(this.width / 2 - 25 - 100 - 54, this.height / 6 + 72, 50, 20, new TranslatableComponent("jukeboxmc.screen.reset"), (p_96257_) -> {
            jukeboxMC.jukeboxURL = "http://crafty.lowkeys.de:8000/Jukebox";
            this.jukeboxURLEdit.setValue(jukeboxMC.jukeboxURL);
        }));

        this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20, CommonComponents.GUI_DONE, (p_96257_) -> {
            jukeboxMC.inGamePrefix = this.inGamePrefixEdit.getValue();
            jukeboxMC.jukeboxPrefix = this.jukeboxPrefixEdit.getValue();
            jukeboxMC.key = this.keyEdit.getValue();
            jukeboxMC.jukeboxURL = this.jukeboxURLEdit.getValue();
            this.minecraft.setScreen(this.lastScreen);
        }));

    }

    @Override
    public void tick() {

        this.inGamePrefixEdit.tick();
        this.jukeboxPrefixEdit.tick();
        this.keyEdit.tick();
        this.jukeboxURLEdit.tick();


    }

    @Override
    public void render(@NotNull PoseStack p_96562_, int p_96563_, int p_96564_, float p_96565_) {
        this.renderBackground(p_96562_);
        drawCenteredString(p_96562_, this.font, this.title, this.width / 2, 15, 16777215);

        drawString(p_96562_, this.font, new TranslatableComponent("jukeboxmc.ingamePrefix.description"), this.width / 2 - 71, this.height / 6 + 7, 16777215);
        drawString(p_96562_, this.font, new TranslatableComponent("jukeboxmc.jukeboxPrefix.description"), this.width / 2 - 71, this.height / 6 + 24 + 7, 16777215);
        drawString(p_96562_, this.font, new TranslatableComponent("jukeboxmc.key.description"), this.width / 2 + 79, this.height / 6 + 48 + 7, 16777215);
        drawString(p_96562_, this.font, new TranslatableComponent("jukeboxmc.jukeboxURL.description"), this.width / 2 + 79, this.height / 6 + 72 + 7, 16777215);

        this.inGamePrefixEdit.render(p_96562_, p_96563_, p_96564_, p_96565_);
        this.jukeboxPrefixEdit.render(p_96562_, p_96563_, p_96564_, p_96565_);
        this.keyEdit.render(p_96562_, p_96563_, p_96564_, p_96565_);
        this.jukeboxURLEdit.render(p_96562_, p_96563_, p_96564_, p_96565_);

        super.render(p_96562_, p_96563_, p_96564_, p_96565_);
    }


    @Override
    public void onClose() {

        jukeboxMC.inGamePrefix = this.inGamePrefixEdit.getValue();
        jukeboxMC.jukeboxPrefix = this.jukeboxPrefixEdit.getValue();
        jukeboxMC.key = this.keyEdit.getValue();
        jukeboxMC.jukeboxURL = this.jukeboxURLEdit.getValue();

    }
}
