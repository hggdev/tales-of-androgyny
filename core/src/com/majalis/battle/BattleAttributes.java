package com.majalis.battle;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.majalis.character.Stance;

/*
 * Represents the info about a battle for saving and loading.
 */
public class BattleAttributes {

	private final BattleCode battleCode;
	private final ObjectMap<String, Integer> outcomes;
	private final Stance playerStance;
	private final Stance enemyStance;
	private final boolean disarm;
	private final int climaxCounter;
	
	@SuppressWarnings("unused")
	private BattleAttributes() { this(null, null, null, null, false, 0); }
	
	public BattleAttributes(BattleCode battleCode, ObjectMap<String, Integer> outcomes, Stance playerStance, Stance enemyStance, boolean disarm, int climaxCounter) {
		this.battleCode = battleCode;
		this.outcomes = outcomes;
		this.playerStance = playerStance;
		this.enemyStance = enemyStance;
		this.disarm = disarm;
		this.climaxCounter = climaxCounter;
	}

	public Array<AssetDescriptor<?>> getRequirements() {
		return battleCode.getRequirements();
	}
	
	protected BattleCode getBattleCode() {
		return battleCode;
	}

	protected Stance getPlayerStance() {
		return playerStance;
	}
	
	protected Stance getEnemyStance() {
		return enemyStance;
	}
	
	protected boolean getDisarm() {
		return disarm;
	}
	
	protected int getClimaxCounter() {
		return climaxCounter;
	}
	
	protected ObjectMap<String, Integer> getOutcomes() {
		return outcomes;
	}

	public AssetDescriptor<Music> getMusic() {
		return battleCode.getMusic();
	}
}
