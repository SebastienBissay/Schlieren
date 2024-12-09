import processing.core.PApplet;
import processing.core.PVector;

import static parameters.Parameters.*;
import static save.SaveUtil.saveSketch;

public class Schlieren extends PApplet {
    public static void main(String[] args) {
        PApplet.main(Schlieren.class);
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
        randomSeed(SEED);
        noiseSeed(floor(random(MAX_INT)));
    }

    @Override
    public void setup() {
        background(BACKGROUND_COLOR.red(), BACKGROUND_COLOR.green(), BACKGROUND_COLOR.blue());
        stroke(STROKE_COLOR.red(), STROKE_COLOR.green(), STROKE_COLOR.blue(), STROKE_COLOR.alpha());
        noFill();
        noLoop();
    }

    @Override
    public void draw() {
        for (int y = 0; y < height; y++) {
            for (float z = 0; z < 1; z += 0.1) {
                PVector p = new PVector(1, y, z);

                for (int k = 0; k < LENGTH; k++) {
                    point(p.x, p.y);

                    float a = schlierenNoise(NOISE_SCALE * p.x, NOISE_SCALE * p.y);
                    float b = schlierenNoise(NOISE_SCALE * p.x, p.z);
                    float c = schlierenNoise(NOISE_SCALE * p.y, p.z);
                    PVector speed;
                    speed = new PVector
                            (cos(a) * cos(b) + (cos(a) * sin(b) * sin(c) - sin(a) * cos(c)) + (cos(a) * sin(b) * cos(c) + sin(a) * sin(c)),
                                    sin(a) * cos(b) + (sin(a) * sin(b) * sin(c) + cos(a) * cos(c)) + (sin(a) * sin(b) * cos(c) - cos(a) * sin(c)),
                                    0.01f * (-sin(b) + cos(b) * sin(c) + cos(b) * cos(c)));
                    p.add(speed);

                    if (p.x > width || p.x < 0 || p.y > height || p.y < 0) {
                        break;
                    }
                    p.z %= 1;
                    if (p.z < 0) {
                        p.z++;
                    }
                }
            }
        }
        saveSketch(this);
    }

    float schlierenNoise(float x, float y) {
        return TWO_PI * pow(noise(x, y), NOISE_EXPONENT);
    }
}
