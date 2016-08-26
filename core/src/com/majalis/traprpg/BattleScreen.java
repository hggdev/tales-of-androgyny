package com.majalis.traprpg;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;

public class BattleScreen extends AbstractScreen{

	public static final ObjectMap<String, Class<?>> resourceRequirements = new ObjectMap<String, Class<?>>();
	static {
		resourceRequirements.put("uiskin.json", Skin.class);
		resourceRequirements.put("wereslut.png", Texture.class);
		resourceRequirements.put("sound.wav", Sound.class);
	}
	
	private final SaveService saveService;
	private final Battle battle;
	
	protected BattleScreen(ScreenFactory screenFactory, ScreenElements elements, SaveService saveService, Battle battle) {
		super(screenFactory, elements);
		this.saveService = saveService;
		this.battle = battle;
	}

	@Override
	public void buildStage() {
		addActor(battle);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		battle.battleLoop();
		if (battle.battleOver){			
			saveService.saveDataValue("Context", SaveManager.GameContext.ENCOUNTER);
			if (battle.victory){
				saveService.saveDataValue("SceneCode", battle.getVictoryScene());
			}
			else {
				saveService.saveDataValue("SceneCode", battle.getDefeatScene());
			}
			showScreen(ScreenEnum.ENCOUNTER);
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
		super.draw();
		batch.end();
	}
	
}