package edu.neumont.csc;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.input.*;


/**
 * This is the Main Class of your Game. It should boot up your game and do initial initialisation
 * Move your Logic into AppStates or Controls or other java classes
 */
public class Example extends SimpleApplication {

    public static void main(String[] args) {
        Example app = new Example();
        app.setShowSettings(false); //Settings dialog not supported on mac
        app.start();
    }

    final private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float fps) {
            if (isPressed) {
                if (name.equals("P")) {
                    //Lock Camera position
                    flyCam.setEnabled(true);
                    flyCam.onAnalog("FLYCAM_StrafeLeft", 5f, 300);
                }
                if (name.equals("MouseLeftClick")) {
                    clickMouse();
                }
            }
        }
    };

    private void clickMouse() {
        CollisionResults results = new CollisionResults();
        Vector2f cursorPosition = inputManager.getCursorPosition();
        Vector3f click3d = cam.getWorldCoordinates(cursorPosition, 0f);

        Vector3f direction = cam.getWorldCoordinates(cursorPosition, 1f).subtractLocal(click3d).normalizeLocal();


        Ray ray = new Ray(click3d, direction);
        rootNode.collideWith(ray, results);

        Geometry geometry = results.getCollision(0).getGeometry();
        System.out.println(geometry.getName());

        //Sets the local translation to (3,3,3)
        geometry.setLocalTranslation(new Vector3f(3, 3, 3));

        //From the local translation (3,3,3), it will move it another 3 units
        //Making the local translation (6,6,6)
        geometry.move(new Vector3f(3, 3, 3));

        //Returns the translation of the geometry in relation to world coordinates
        geometry.getWorldTranslation();
    }
    private void addTriggers() {
        inputManager.addMapping("P", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(actionListener, "P");

        inputManager.addMapping("MouseLeftClick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "MouseLeftClick");
    }

    @Override
    public void simpleInitApp() {
        //Box b = new Box(1, 1, 1);
        //Geometry geom = new Geometry("Box", b);

        //Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //mat.setColor("Color", ColorRGBA.Blue);
        //geom.setMaterial(mat);
        setupLight();
        createAsset();
        addTriggers();

        //rootNode.attachChild(geom);
        updateCamera();
    }

    private void updateCamera() {
        flyCam.onAnalog("FLYCAM_StrafeRight", 0.02f, 300);
    }

    private void createAsset() {
        assetManager.registerLocator("/assets", FileLocator.class);
        Spatial object = assetManager.loadModel("Models/RookFileW.glb");
        rootNode.attachChild(object);
    }

    private void setupLight() {
        /**
         * This shines from the top left corner of the scene, shining down
         * The normalizeLocal is just normalizing the Vector, thereas creating a unit vector with same direction
         * <a href="https://stackoverflow.com/questions/10002918/what-is-the-need-for-normalizing-a-vector">My research into why it's needed for 3d rendering</a>
         */

        DirectionalLight dl1 = new DirectionalLight();
        dl1.setColor(ColorRGBA.Red);
        dl1.setDirection(new Vector3f(-3, 5, 3).normalizeLocal());
        rootNode.addLight(dl1);

        DirectionalLight dl2 = new DirectionalLight();
        dl2.setDirection(new Vector3f(3, 5, 3).normalizeLocal());
        rootNode.addLight(dl2);

        DirectionalLight dl3 = new DirectionalLight();
        dl3.setDirection(new Vector3f(-3, 5, -3).normalizeLocal());
        rootNode.addLight(dl3);

        DirectionalLight dl4 = new DirectionalLight();
        dl4.setDirection(new Vector3f(3, -5, 3).normalizeLocal());
        rootNode.addLight(dl4);
    }


    @Override
    public void simpleUpdate(float tpf) {
        //updateCamera();

        LightList list = rootNode.getLocalLightList();
        if (list.get(0).getClass().equals(DirectionalLight.class)) {

            DirectionalLight light = ((DirectionalLight)list.get(0));
            Vector3f currDirection = light.getDirection();
            light.setDirection(new Vector3f((currDirection.x - 0.004f), currDirection.y, currDirection.z));
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //add render code here (if any)
    }
}
