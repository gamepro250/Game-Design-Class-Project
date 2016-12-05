package com.billyharrisongdx.game.util ;

import com.badlogic.gdx.physics.box2d.Contact ;
import com.badlogic.gdx.physics.box2d.ContactImpulse ;
import com.badlogic.gdx.physics.box2d.ContactListener ;
import com.badlogic.gdx.physics.box2d.Fixture ;
import com.badlogic.gdx.physics.box2d.Manifold ;
import com.badlogic.gdx.utils.ObjectMap ;
import com.billyharrisongdx.game.game.WorldController ;
import com.billyharrisongdx.game.game.Assets ;
import com.billyharrisongdx.game.game.objects.AbstractGameObject ;
import com.billyharrisongdx.game.game.objects.Ice ;
import com.billyharrisongdx.game.game.objects.Ground ;
import com.billyharrisongdx.game.game.objects.Character ;
import com.billyharrisongdx.game.game.objects.Fire ;
import com.billyharrisongdx.game.game.objects.Goal;

public class CollisionHandler implements ContactListener
{
    private ObjectMap<Short, ObjectMap<Short, ContactListener>> listeners;

    private WorldController world;

    public CollisionHandler(WorldController w)
    {
    	world = w;
        listeners = new ObjectMap<Short, ObjectMap<Short, ContactListener>>();
    }

    public void addListener(short categoryA, short categoryB, ContactListener listener)
    {
        addListenerInternal(categoryA, categoryB, listener);
        addListenerInternal(categoryB, categoryA, listener);
    }

    @Override
    public void beginContact(Contact contact)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        AbstractGameObject objA = (AbstractGameObject)fixtureA.getBody().getUserData();
        AbstractGameObject objB = (AbstractGameObject)fixtureB.getBody().getUserData();

        processContact(contact);

       		ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
	        if (listener != null)
	        {
	            listener.beginContact(contact);
	        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
        if (listener != null)
        {
            listener.preSolve(contact, oldManifold);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
        if (listener != null)
        {
            listener.postSolve(contact, impulse);
        }
    }

    @Override
    public void endContact(Contact contact)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
        if (listener != null)
        {
            listener.endContact(contact);
        }
    }

    private void addListenerInternal(short categoryA, short categoryB, ContactListener listener)
    {
        ObjectMap<Short, ContactListener> listenerCollection = listeners.get(categoryA);

        if (listenerCollection == null)
        {
            listenerCollection = new ObjectMap<Short, ContactListener>();
            listeners.put(categoryA, listenerCollection);
        }

        listenerCollection.put(categoryB, listener);
    }

    private ContactListener getListener(short categoryA, short categoryB)
    {
        ObjectMap<Short, ContactListener> listenerCollection = listeners.get(categoryA);

        if (listenerCollection == null)
        {
            return null;
        }

        return listenerCollection.get(categoryB);
    }

    private void processContact(Contact contact)
    {
    	Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        AbstractGameObject objA = (AbstractGameObject)fixtureA.getBody().getUserData();
        AbstractGameObject objB = (AbstractGameObject)fixtureB.getBody().getUserData();

        if (objA instanceof Character)
        {
        	processPlayerContact(fixtureA, fixtureB);
        }
        else if (objB instanceof Character)
        {
        	processPlayerContact(fixtureB, fixtureA);
        }
    }

    /**
     * Determines what happens to the player, and the object it collides with
     */
    private void processPlayerContact(Fixture playerFixture, Fixture objFixture)
    {
    	if (objFixture.getBody().getUserData() instanceof Ground)
    	{
    		world.resetJump() ;
    		world.level.character.grounded = true ;
    		world.level.character.airborne = false ;
    	}
    	else if (objFixture.getBody().getUserData() instanceof Ice)
    	{
    		AudioManager.instance.play(Assets.instance.sounds.pickupIce) ;
    		world.score += Ice.getScore() ;

    		Ice ice = (Ice)objFixture.getBody().getUserData() ;
    		world.flagForRemoval(ice) ;

    	}
    	else if (objFixture.getBody().getUserData() instanceof Fire)
    	{
    		AudioManager.instance.play(Assets.instance.sounds.pickupFire) ;
    		world.score += Fire.getScore() ;
    		Character.setFirePowerup(true) ;

    		Fire fire = (Fire)objFixture.getBody().getUserData() ;
    		world.flagForRemoval(fire) ;
    	}
    	else if (objFixture.getBody().getUserData() instanceof Goal)
    	{
    		world.level.goalReached = true ;
    	}
    }

}
