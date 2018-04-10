package com.unical.crazystackers;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class CrazyStackers extends ApplicationAdapter {
    SpriteBatch batch;
    ShapeRenderer shaper;

    final int ASTROIDCOUNT = 5;
    float x, y, rot, speedX, speedY;
    float[] astroidX, astroidY, astroidRot, astroidXSpeed, astroidYSpeed;


    @Override
    public void create() {
        batch = new SpriteBatch();
//        img = new Texture("badlogic.jpg");
        shaper = new ShapeRenderer();

        x = 200;
        y = 200;
        rot = 0;
        speedX = 0;
        speedY = 0;
        astroidX = new float[ASTROIDCOUNT];
        astroidY = new float[ASTROIDCOUNT];
        astroidRot = new float[ASTROIDCOUNT];
        astroidXSpeed = new float[ASTROIDCOUNT];
        astroidYSpeed = new float[ASTROIDCOUNT];
        for (int i = 0; i < ASTROIDCOUNT; i++) {
            astroidX[i] = 100 + MathUtils.random() * 400;
            astroidY[i] = 100 + MathUtils.random() * 400;
            astroidRot[i] = MathUtils.random() * 359;
            astroidXSpeed[i] = 0.5f - MathUtils.random();
            astroidYSpeed[i] = 0.5f - MathUtils.random();
        }
    }



    @Override
    public void render() {

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            rot++;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            rot--;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            //increase speed
            speedX += 0.01 * MathUtils.cosDeg(rot);
            speedY += 0.01 * MathUtils.sinDeg(rot);
        }

        //apply speed
        x += speedX;
        y += speedY;
        //slow down
        speedX *= 0.992;
        speedY *= 0.992;

        //apply astroid speed
        for (int i = 0; i < ASTROIDCOUNT; i++) {
            astroidX[i] += astroidXSpeed[i];
            astroidY[i] += astroidYSpeed[i];
            astroidRot[i]++; //and rotate them
        }

        Gdx.gl.glClearColor(0.1f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shaper.begin(ShapeRenderer.ShapeType.Filled);
        shaper.identity(); // reset
        shaper.translate(x, y, 0); // move
        shaper.rotate(0, 0, 1, rot); //rotate
        shaper.setColor(1, 0, 0, 1);
        shaper.circle(0, 0, 10);
        shaper.setColor(1, 1, 0, 1);
        shaper.line(-10, 0, 20, 0);
        shaper.setColor(1, 0, 1, 1);
        shaper.line(0, -10, 0, 10);

        for (int i = 0; i < ASTROIDCOUNT; i++) {
            shaper.identity(); // reset
            shaper.translate(astroidX[i], astroidY[i], 0); // move
            shaper.rotate(0, 0, 1, astroidRot[i]); //rotate
            shaper.setColor(1, 0, 0, 1);
            shaper.rect(-10, -10, 20, 20);
        }


        shaper.end();
    }
}