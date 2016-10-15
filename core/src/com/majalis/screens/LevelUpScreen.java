package com.majalis.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;
import com.majalis.asset.AssetEnum;
import com.majalis.encounter.Encounter;
import com.majalis.save.SaveEnum;
import com.majalis.save.SaveManager;
import com.majalis.save.SaveService;

public class LevelUpScreen extends AbstractScreen {

	public static final ObjectMap<String, Class<?>> resourceRequirements = new ObjectMap<String, Class<?>>();
	static {
		resourceRequirements.put("uiskin.json", Skin.class);
		resourceRequirements.put("sound.wav", Sound.class);
		resourceRequirements.put(AssetEnum.CHARACTER_SCREEN.getPath(), Texture.class);
	}
	
	private final AssetManager assetManager;
	private final SaveService saveService;
	private final Encounter encounter;
	protected LevelUpScreen(ScreenFactory screenFactory, ScreenElements elements, AssetManager assetManager, SaveService saveService, Encounter encounter) {
		super(screenFactory, elements);
		this.assetManager = assetManager;
		this.saveService = saveService;
		this.encounter = encounter;
	}

	@Override
	public void buildStage() {
		for (Actor actor: encounter.getActors()){
			this.addActor(actor);
		}  
	}
	

	@Override
	public void render(float delta) {
		super.render(delta);
		encounter.gameLoop();
		if (encounter.encounterOver){
			saveService.saveDataValue(SaveEnum.CONTEXT, SaveManager.GameContext.WORLD_MAP);
			showScreen(ScreenEnum.CHARACTER);
		}
		if (encounter.gameExit){
			showScreen(ScreenEnum.MAIN_MENU);
		}
		else {
			draw();
		}
	}
	
	public void draw(){
		batch.begin();
		OrthographicCamera camera = (OrthographicCamera) getCamera();
        batch.setTransformMatrix(camera.view);
		batch.setProjectionMatrix(camera.combined);
		camera.update();
		batch.end();
	}

	@Override
	public void dispose() {
		for(String path: resourceRequirements.keys()){
			if (path.equals("sound.wav")) continue;
			assetManager.unload(path);
		}
	}
	
}