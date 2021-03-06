/**
 * NEED TO BE FIXED!
 * NOTE: The setLevel(int level) function has a bug. When setLevel() 
 * called, it displays 'Score: _' instead of 'Level _' 
 * E.g. setLevel(x) =>  Score: x (on the game window)
 */

/**
 * ***********************************************
 * **** LIST OF ALL FUNCTIONAL API FUNCTIONS *****
 * **** ******************************************
 * 
 * BALL:
 * --  void ballSetToInvisible(int whichBall)
 * --  boolean ballGetVisibility(int whichBall)
 * --  void ballSetState(PaddleAndBallStatesEnum state, int whichBall)
 * --  PaddleAndBallStatesEnum ballGetState(int whichBall)
 * --  Ball.BallType toBallState(PaddleAndBallStatesEnum input)
 * --  void ballPlayBounceSound()
 * 
 * PADDLE:
 * --  PaddleAndBallStatesEnum paddleGetState(int whichPaddle)
 * --  PaddleAndBallStatesEnum paddleStateTranslate(Paddle.PaddleState input)
 * --  void paddleSetCenterX(float x, int whichPaddle)
 * --  void paddleSetImage(String fileName, int whichPaddle)
 * --  void paddleMoveLeft(int whichPaddle)
 * --  void paddleMoveRight(int whichPaddle)
 * 
 * BLOCK:
 * --  boolean areAllBlocksRemoved()
 * --  void removeNeighboringBlocks(int row, int col, Block.BlockType type)
 * 
 * WALL:
 * --  WallsEnum getCollidedWall(int whichBall)
 * --  WallsEnum toWallsEnum(BoundCollidedStatus input)
 * 
 * BALL AND PADDLE:
 * --  void ballSpawnNearPaddle(int whichBall, int whichPaddle)
 * --  boolean ballCollidedWithPaddle(int whichBall, int whichPaddle)
 * --  void ballReflectOffPaddle(int whichBall, int whichPaddle)
 * 
 * BALL AND WALL:
 * --  BoundCollidedStatus getBallAndWallCollisionStatus(int whichBall)
 * --  void ballReflectOffTopWall(int whichBall)
 * --  void ballReflectOffLeftWall(int whichBall)
 * --  void ballReflectOffRightWall(int whichBall)
 * --  void ballReflectOffBottomWall(int whichBall)
 * 
 * BALL AND BLOCK:
 * --  boolean ballCollidedWithBlock(int whichBall)
 * --  void handleBlockBallCollision(int whichBall, int whichPaddle)
 * 
 * MISCELLANEOUS:
 * --  void addToBallSetAndSpawn(Ball ball, int whichPaddle)
 * --  void loseALife()
 * --  void lostGameCheck()
 * --  MOUSE AND KEYBOARD:
 *      - boolean isMouseOnScreen()
 *      - float getMouseXCoordinate()
 *      - float getMouseYCoordinate()
 *      - boolean isKeyboardButtonDown(KeysEnum key)    
 *      - boolean isMouseButtonDown(MouseClicksEnum button)
 */
package SpaceSmasherProceduralAPI;
import static SpaceSmasherProceduralAPI.PaddleAndBallStatesEnum.FIRE;
import static SpaceSmasherProceduralAPI.PaddleAndBallStatesEnum.ICE;
import static SpaceSmasherProceduralAPI.PaddleAndBallStatesEnum.NORMAL;
import static SpaceSmasherProceduralAPI.WallsEnum.*;
import java.awt.event.KeyEvent;
import SpaceSmasher.Ball;
import SpaceSmasher.Block;
import SpaceSmasher.Block.BlockState;
import SpaceSmasher.Block.BlockType;
import SpaceSmasher.Paddle.PaddleState;
import SpaceSmasher.SpaceSmasher;
import SpaceSmasher.Paddle;
import SpaceSmasher.Trap;
import Engine.GameWindow;
import Engine.Vector2;
import Engine.World;
import Engine.World.BoundCollidedStatus;
import SpaceSmasherProceduralAPI.WallsEnum.*;


/**
 * SpaceSmasherProceduralAPI
 *  
 * A functional CS1 wrapper around an Object-Oriented Java game
 * 
 * Important Methods:
 *  
 * -- buildGame(): used to create a "level" for our brick-breaking, 
 *    space smashing game. 
 *
 * -- updateGame(): where interactivity, responses to collisions, 
 *    ball spawning, and other game logic will go. 
 * 
 * 
 * Authors: Kelvin Sung, Mike Panitz, Rob Nash, Vuochly Ky (Nin)
 * 
 * 
 */
public class SpaceSmasherProceduralAPI extends SpaceSmasher {   
    
    /**
     * Used in the wrapper API function isKeyboardButtonDown, this array maps a shorter abbreviation like RIGHT to the virtual key event KeyEvent.VK_RIGHT
     */
    public static final int[] keyEventMap = {KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_SPACE, KeyEvent.VK_ENTER, 
        KeyEvent.VK_ESCAPE, KeyEvent.VK_SHIFT, KeyEvent.VK_LESS, KeyEvent.VK_GREATER,KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4,
        KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8, KeyEvent.VK_9, KeyEvent.VK_A,KeyEvent.VK_B,KeyEvent.VK_C,KeyEvent.VK_D,KeyEvent.VK_E,KeyEvent.VK_F,KeyEvent.VK_G,KeyEvent.VK_H,KeyEvent.VK_I,KeyEvent.VK_J,KeyEvent.VK_K,KeyEvent.VK_L,KeyEvent.VK_M,KeyEvent.VK_N,KeyEvent.VK_O,KeyEvent.VK_P,KeyEvent.VK_Q,KeyEvent.VK_R,KeyEvent.VK_S,KeyEvent.VK_T,KeyEvent.VK_U,KeyEvent.VK_V,KeyEvent.VK_W,KeyEvent.VK_X,KeyEvent.VK_Y,KeyEvent.VK_Z,
        KeyEvent.VK_A,KeyEvent.VK_B,KeyEvent.VK_C,KeyEvent.VK_D,KeyEvent.VK_E,KeyEvent.VK_F,KeyEvent.VK_G,KeyEvent.VK_H,KeyEvent.VK_I,KeyEvent.VK_J,KeyEvent.VK_K,KeyEvent.VK_L,KeyEvent.VK_M,KeyEvent.VK_N,KeyEvent.VK_O,KeyEvent.VK_P,KeyEvent.VK_Q,KeyEvent.VK_R,KeyEvent.VK_S,KeyEvent.VK_T,KeyEvent.VK_U,KeyEvent.VK_V,KeyEvent.VK_W,KeyEvent.VK_X,KeyEvent.VK_Y,KeyEvent.VK_Z};
        
    /**
     * PROCEDURAL API - Being called in initialize() function
     * Initialize the game. To be overriden to add extra features.
     */
    public void buildGame(){ }
    
    /**
     * PROCEDURAL API - update the game. Being called in update() function
     */
    public void updateGame(){ }
    
    /**
     * PROCEDURAL API - Set the number of lives for the game
     * @param numOfLife the number of lives
     */
    public void initializeLifeSet(int numOfLife){
        lifeSet.add(numOfLife);
    }
    
    /**
     * PROCEDURAL API - initialize the ball. Adding the ball to the ball set
     * to get them ready to spawn
     * @param numOfBall how many balls to initialize
     */
    public void initializeBallSet(int numOfBall){
        ballSet.add(numOfBall);
        for(int i = 0; i < numOfBall; i++){
            ballSetToInvisible(i);
        }
    }

    /**
     * PROCEDURAL API - Initialize the paddle
     * @param numOfPaddle how many paddle to use in the game
     */
    public void initializePaddleSet(int numOfPaddle){
        paddleSet.add(numOfPaddle);
    }
    
    /**
     * PROCEDURAL API - Overload Rob's function to make it easy to understand and 
     * to show the use of for-loop. There are 3 steps in this function.
     * 
     * This function creates:
     * 7 blocks per row
     * Each row has the order of:
     *      1 ICE block, 2 FIRE blocks, 3 NORMAL blocks, 1 FIRE block
     *      (1 + 2 + 3 + 1 = 7 blocks)
     */
    public void initializeBlockSet(){
        // STEP1: Set how many blocks per row
        setBlocksPerRow(7);
        
        // STEP2: Add blocks to the row
        // The total number of blocks you add must be >= to the 
        // number of blocks you set per row (Refer to the function comment)
        for(int row = 0; row < 3; row++){
            addBlock("ice", 1);
            addBlock("Fire", 2);
            addBlock("Normal", 3);
            addBlock("fire", 1);
        }
        
        // STEP3: Reveal the block power
        revealBlockPower();
    }
    
    // These function are for initializing the block set.
    //  Break the initializeBlockSet function into smaller functions
    /**
     * PROCEDURAL API - set how many blocks to have in a row
     * Note: Must use with addBlock(blockType, numOfBlock) and revealBlockPower()
     * @param numOfBlock how many blocks should be in a row
     */
    public void setBlocksPerRow(int numOfBlock){
        blockSet.setBlocksPerRow(numOfBlock);
    }
    /**
     * PROCEDURAL API - Add blocks to the game. There are 3 types of block that
     * the user can add such as Fire, Ice, or Normal block.
     * Note: Must use with setBlocksPerRow(numOfBlock) and revealBlockPower()
     * @param blockType the type of block to add
     * @param numOfBlock how many block of this type
     */
    public void addBlock(String blockType, int numOfBlock){
        if(blockType.equalsIgnoreCase("fire"))
            blockSet.addFireBlock(numOfBlock);
        else if(blockType.equalsIgnoreCase("ice"))
            blockSet.addFreezingBlock(numOfBlock);
        else if(blockType.equalsIgnoreCase("normal"))
            blockSet.addNormalBlock(numOfBlock);
    }
    /**
     * PROCEDURAL API - Reveal the power of the block
     * E.g. If the type is Fire, display the block as Fire block 
     * Note: Must use with setBlocksPerRow(numOfBlock) and addBlock(blockType, numOfBlock)
     */
    public void revealBlockPower(){
        for (int iRow = 0; iRow < blockSet.getNumRows(); iRow++) {
            for (int iCol = 0; iCol < blockSet.getNumColumns(); iCol++) {
                Block nextBlock = blockSet.getBlockAt(iRow, iCol);
                if(nextBlock != null)
                    nextBlock.revealPower();
            }
        }
    }
    
    /**
     * PROCEDURAL API - Clear the blocks in the block set
     * Required if the user wishes to add a new set of blocks. For example, if they are 
     * transitioning to a new level. 
     */
    public void clearBlockSet()
    {
        blockSet.clear();
    }
    
    // use specific keys for paddle movement
    /**
     * PROCEDURAL API - Function for paddle movement control. 
     * Use the A and D -keys to move the given paddle
     * @param whichPaddle do you want to use these keys to control
     */
    public void useADForPaddleMovement(int whichPaddle){
        if(isKeyboardButtonDown(KeysEnum.A) || isKeyboardButtonDown(KeysEnum.a)){
            paddleMoveLeft(whichPaddle);
        }
        if(isKeyboardButtonDown(KeysEnum.D) || isKeyboardButtonDown(KeysEnum.d)){
            paddleMoveRight(whichPaddle);
        }
    }
    
    /**
     * PROCEDURAL API - Function for paddle movement control
     * Use mouse to control the movement of the given paddle
     * @param whichPaddle do you want to use the mouse to control
     */
    public void useMouseForPaddleMovement(int whichPaddle){
        if(isMouseOnScreen()){
            paddleSetCenterX(getMouseXCoordinate(), whichPaddle);   
        }
    }
    
    /**
     * PROCEDURAL API - To spawn a ball from a paddle by using the SPACE key or the 
     * mouse left click
     * @param whichBall do you want to spawn
     * @param whichPaddle do you want to spawn the ball from
     */
    public void spawnBall(int whichBall, int whichPaddle){
        if (isKeyboardButtonDown(KeysEnum.SPACE) || isMouseLeftClicked()) {       
           if(!ballIsVisible(whichBall)){
               ballSpawnNearPaddle(whichBall, whichPaddle);
           }
         }
    }
    
    /**
     * PROCEDURAL API - in addition to the API utility method, ballPlayBounceSound()
     * @param whichBall ball to play the sound for
     */
    public void ballPlayBounceSound(int whichBall) {
        ballSet.get(whichBall).playBounceSound();
    }
    
    /**
     * PROCEDURAL API - Check if the given ball is visible.
     * @param whichBall the ball to check
     * @return True if the ball is visible, false otherwise
     */
    public boolean ballIsVisible(int whichBall){
        Ball ball = ballSet.get(whichBall);
        return (ball != null && ball.isVisible());
    }
    
    /**
     * PROCEDURAL API - Check if user pressed on the left button of the mouse
     * @return True if the mouse left button is pressed, False otherwise
     */
    public boolean isMouseLeftClicked(){
        return isMouseOnScreen() && mouse.isButtonTapped(1);
    }
    
    /**
     * PROCEDURAL API - remove only the block that is hit by the ball
     * @param whichBall the ball to handle for
     */
    protected void handleBlockBallCollision(int whichBall){
        if (ballCollidedWithBlock(whichBall)) {  
            Ball ball = ballSet.get(whichBall);
            Block block = blockSet.getCollidedBlock(ball);
            block.reflect(ball);
            ballPlayBounceSound(whichBall);
            blockSet.remove(block);
            increaseScore(1);   // Update the score}
        }
   }
    
    /**
     * PROCEDURAL API - Handle when the given ball hit the wall
     * @param whichBall ball to check the collision for
     */
    protected void ballAndWallCollisionCheck(int whichBall) {
        WallsEnum wallThatWasHit = getCollidedWall(whichBall);
        
        if (wallThatWasHit == TOP) {                     
            ballReflectOffTopWall(whichBall);
            ballPlayBounceSound(whichBall);
        } else if(wallThatWasHit == BOTTOM) {
            ballSetToInvisible(whichBall);  
            ballPlayBounceSound(whichBall);
            loseALife();
        } else if(wallThatWasHit == WallsEnum.LEFT) {
            ballReflectOffLeftWall(whichBall);
            ballPlayBounceSound(whichBall);
        } else if(wallThatWasHit == WallsEnum.RIGHT) {
            ballReflectOffRightWall(whichBall);
            ballPlayBounceSound(whichBall);
        }
    }
    
    /**
     * PROCEDURAL API - Handle when ball-block and ball-wall collision. This function also
     * check the game is lost by checking if all the lives are used
     * @param handle collision for whichBall
     */
    public void handleBlockBallWallCollision(int whichBall){
        if(ballIsVisible(whichBall)){
            handleBlockBallCollision(whichBall);
            ballAndWallCollisionCheck(whichBall);
            lostGameCheck();
        }
    }
    
    /**
     * PROCEDURAL API - Handle when ball hit the paddle.
     * @param handle collision for whichBall and whichPaddle
     */
    public void handlePaddleBallCollisions(int whichBall, int whichPaddle){
        if(ballIsVisible(whichBall) && ballCollidedWithPaddle(whichBall, whichPaddle)){
            ballReflectOffPaddle(whichBall, whichPaddle);
            ballPlayBounceSound(whichBall);
        }
    }
    
    /**
     * PROCEDURAL API - Method to add a ball to the ballSet and Spawn on the screen
     * @param whichPaddle which paddle to add the ball to
     */
    protected void addBall(int whichPaddle){
        Ball ball = new Ball();
        addToBallSetAndSpawn(ball, whichPaddle);
    }
    
    //*****************************************************************************

    /**
     * This method could be overridden to add extra features
     * specific to initialization of the starting number of lives (lifeSet), the starting
     * number of paddles (in the paddleSet), the number of balls in our simulation (inside ballSet)
     * the number and arrangement of blocks on the screen (in the blockSet) and finally console reporting
     */
    protected void initialize() {
        buildGame();
    }
    
    /**
     * This method could be overridden to add extra features
     * Specific to winning the game, paddle checks and ball checks
     */
    protected void update() {
        updateGame();
    }

   /**
    * API utility method
    * This method is responsible for determining if all blocks have been removed and we've  
    * met our winning condition for the game 
    * 
    * @return - true if all blocks have been destroyed on screen
    */
    protected boolean areAllBlocksRemoved() {      
         boolean retVal = true;
         for(int nextRow = 0; nextRow < blockSet.getNumRows(); nextRow++) {
             for(int nextColumn = 0; nextColumn < blockSet.getNumColumns(); nextColumn++) {
                 Block nextBlock = blockSet.getBlockAt(nextRow, nextColumn);
                 if (nextBlock != null && nextBlock.getType() != Block.BlockType.EMPTY) { 
                     retVal = false;
                     break;
                 }       
             }
         }
         return retVal;
    }
    
    /**
     * This method adds the given ball to the ballSet and spawn it from the 
     * first paddle of the paddleSet
     * @param ball - the ball to add to the ballSet and also spawn on screen
     */
    protected void addToBallSetAndSpawn(Ball ball) {
        addToBallSetAndSpawn(ball, 0);
    }
    
    /**
     * This method adds the given ball to the ballSet and spawn it from the given paddle
     * @param ball - the ball to add to the ballSet and also spawn on screen
     * @param whichPaddle - the integer index specifying which paddle inside of the paddleSet should be used
     */
    protected void addToBallSetAndSpawn(Ball ball, int whichPaddle) {
        ballSet.add(ball);
        ball.spawn(paddleSet.get(whichPaddle));
    }
    
    /**
     * Create a new ball
     * @return Ball - the newly created ball for you to use
     */
    protected Ball createNewBall() {
        return new Ball();
    }
    
    /**
     * API utility method
     * Check if the (first) ball in the ballSet collided with any block
     * @return true if the first ball in the ballSet collided with any block
     */
    protected boolean ballCollidedWithBlock() {
        return ballCollidedWithBlock(0);
    }
    /**
     * API utility method
     * Check if the given ball collided with any block
     * @param whichBall - an integer index to specify which ball out of all balls in the ballSet
     * @return true if the ball specified by the parameter collided with any block
     */
    protected boolean ballCollidedWithBlock(int whichBall) {
        return blockSet.isBallCollidingWithABlock(ballSet.get(whichBall));
    }
    
   /**
    * API utility method
    * @param button - the mouse button in question 
    * @return - true if the button specified is being clicked on the mouse
    */
    protected boolean isMouseButtonDown(MouseClicksEnum button){
        return mouse.isButtonTapped(button.ordinal()+1);  //enum-mapping
    }
 
    /**
     * API utility method
     * Bounce first ball in the ballSet off the top wall
     */
    public void ballReflectOffTopWall() {
        ballReflectOffTopWall(0);
    }
    
    /**
     * API utility method
     * Bounce given ball in the ballSet off the top wall
     * @param whichBall - the index used to select the ball to use out of the ballSet
     */
    public void ballReflectOffTopWall(int whichBall) {
        ballSet.get(whichBall).reflectTop();;
    }
    /**
     * API utility method
     * Bounce first ball in the ballSet off the left wall
     */
    public void ballReflectOffLeftWall() {
        ballReflectOffLeftWall(0);
    }
    /**
     * API utility method
     * Bounce given ball in the ballSet off the left wall
     * @param whichBall - the index used to select the ball to use out of the ballSet
     */
    public void ballReflectOffLeftWall(int whichBall) {
        ballSet.get(whichBall).reflectLeft();
    }
    /**
     * API utility method
     * Bounce first ball in the ball set off the right wall
     */
    public void ballReflectOffRightWall() {
        ballReflectOffRightWall(0);
    }
    /**
     * API utility method
     * Bounce given ball in the ballSet off the right wall
     * @param whichBall - the index used to select the ball to use out of the ballSet
     */
    public void ballReflectOffRightWall(int whichBall) {
        ballSet.get(whichBall).reflectRight();
    }
    
    /**
     * API utility method
     * Bounce first ball in the ballSet off the right wall
     */
    public void ballReflectOffBottomWall() {
        ballReflectOffBottomWall(0);
    }
    /**
     * API utility method
     * Bounce given ball in the ballSet off the right wall
     * @param whichBall - the index used to select the ball to use out of the ballSet
     */
    public void ballReflectOffBottomWall(int whichBall) {
        ballSet.get(whichBall).reflectBottom();
    }
    
    /**
     * API utility method
     * Set the (first) ball in the ballSet to invisible
     */
    protected void ballSetToInvisible() {
         ballSetToInvisible(0);
    }
    /**
     * API utility method
     * Set the given ball to invisible
     * @param whichBall - the index used to select the ball to use out of the ballSet
     */
    protected void ballSetToInvisible(int whichBall) {
        ballSet.get(whichBall).setToInvisible();
    }
    /**
     * API utility method
     * Set the (first) ball in the ballSet to the given state
     * @param state - the state you want the ball to be set to
     */
    public void ballSetState(PaddleAndBallStatesEnum state){
        ballSetState(state, 0);
    }
    /**
     * API utility method
     * Set the given ball to the given state
     * @param state - the state you want the ball to be set to
     * @param whichBall - the index used to select the ball to use out of the ballSet
     */
    public void ballSetState(PaddleAndBallStatesEnum state, int whichBall) {
         ballSet.get(whichBall).setType(toBallState(state));
    }
  
    /**
     * API utility method
     * Get the state of the (first) ball in the ballSet
     * @return - the state of the first ball in the ballSet
     */
    public PaddleAndBallStatesEnum ballGetState() {
        return ballGetState(0);
    }
    /**
     * API utility method
     * Get the state of the given ball
     * @param whichBall - the index used to select the ball to use out of the ballSet
     * @return - the state of the ball for use with the Space Smasher CS1 Functional API
     */
    public PaddleAndBallStatesEnum ballGetState(int whichBall) {
        Ball current = ballSet.get(whichBall);
        
        if(current.isBurning()) return PaddleAndBallStatesEnum.FIRE;
        else if(current.isFrozen()) return PaddleAndBallStatesEnum.ICE;
        
        return PaddleAndBallStatesEnum.NORMAL;
    }
    /**
     * API utility method
     * Get the state of the (first) paddle in the paddleSet
     * @return - the state of the first paddle in the paddleSet
     */
    public PaddleAndBallStatesEnum paddleGetState() { 
        return paddleGetState(0);
    }
    /**
     * API utility method
     * Get the state of the given paddle
     * @param whichPaddle - the index used to select the paddle to use out of the paddleSet
     * @return - the state of the paddle specified by the parameter, for use with the mid-level Space Smasher CS1 Functional API
     */
    public PaddleAndBallStatesEnum paddleGetState(int whichPaddle) {
        return paddleStateTranslate(paddleSet.get(whichPaddle).getState());
    }
    
    /**
     * API utility method - functional wrapper
     * @param input - the state for use with the mid-level SpaceSmasher CS1 Functional API wrapper
     * @return - the state for use with the top-level SpaceSmasher API
     */
    public Ball.BallType toBallState(PaddleAndBallStatesEnum input){
        switch(input) {
            case FIRE: return Ball.BallType.FIRE;
            case ICE: return Ball.BallType.ICE;
            default: return Ball.BallType.NORMAL; 
        }
    }
    
    /**
     * API utility method - functional wrapper
     * @param input - the state for use with the top-level SpaceSmasher API
     * @return - the state for use with the mid-level SpaceSmasher CS1 Functional API wrapper
     */
    public PaddleAndBallStatesEnum paddleStateTranslate(Paddle.PaddleState input) {
        switch(input) {
            case FIRE: return PaddleAndBallStatesEnum.FIRE;
            case ICE: return PaddleAndBallStatesEnum.ICE;
            default: return PaddleAndBallStatesEnum.NORMAL; 
        }
    }
    
    /**
     * API utility method
     * Get the collision status of the given ball
     * @param whichBall - the index used to select the ball to use out of the ballSet
     * @return - the status of the ball for use with the top-level Space Smasher API
     */
    protected BoundCollidedStatus getBallAndWallCollisionStatus(int whichBall) {
        return ballSet.get(whichBall).collideWorldBound();
    }
    /**
     * API utility method 
     * Get the collision status with respect to the walls for the first ball in the ballSet
     * @return - the collision status with respect to walls for the first ball in the ballSet
     */
    protected BoundCollidedStatus getBallAndWallCollisionStatus() {
       return getBallAndWallCollisionStatus(0);
    }
    /**
     * API utility method
     * Get the wall that collided with the first ball in the ballSet
     * @return - the wall that collided with the first ball in the ballSet
     */
    protected WallsEnum getCollidedWall() {
        return getCollidedWall(0);
    }
    
    /**
     * API utility method
     * Get the wall that collided with the given ball
     * @param whichBall - the index used to select the ball to use out of the ballSet
     * @return - the wall that was collided with, for use with the mid-level Space Smasher CS1 Functional API
     */
    protected WallsEnum getCollidedWall(int whichBall) {
        return toWallsEnum(getBallAndWallCollisionStatus(whichBall));
    }
    /**
     * API utility method - functional wrapper
     * @param input - the status for use with the top-level SpaceSmasher API
     * @return - the status for use with the SpaceSmasher CS1 Functional API wrapper
     */
    protected WallsEnum toWallsEnum(BoundCollidedStatus input) {
        switch(input) {
            case INSIDEBOUND: return WallsEnum.NO_COLLISION;    
            case LEFT: return WallsEnum.LEFT;
            case RIGHT: return WallsEnum.RIGHT;
            case TOP: return WallsEnum.TOP;
            case BOTTOM: return WallsEnum.BOTTOM;
            
            default: throw new RuntimeException("Bad BoundCollidedStatus: " + input.toString());
        }
    }
    
    /**
     * API utility method
     * Remove a life from the lifeSet
     */
    protected void loseALife() {
        lifeSet.remove();
    }
    
    /**
     * API utility method
     * Check if the game is lost
     */
    protected void lostGameCheck() {
        if(lifeSet.getCount() == 0)
            gameLost();
    }
    
    /**
     * API utility method
     * Check if the mouse is attached to the computer and usable in the game
     * @return - true if the mouse is attached to the computer and usable in our game
     */
    protected boolean isMouseOnScreen() {
        return mouse.MouseOnScreen();
    }
    /**
     * API utility method
     * Get the x position of the mouse cursor
     * @return - the x position of the mouse cursor
     */
    protected float getMouseXCoordinate() {
        return mouse.getWorldX();
    }
    /**
     * API utility method
     * Get the y position of the mouse cursor
     * @return - the y position of the mouse cursor
     */
    protected float getMouseYCoordinate() {
        return mouse.getWorldY();
    }
    
    
    /**
     * API utility method
     * @param x - the x value that will be assigned to the first paddle in the paddleSet 
     */
    protected void paddleSetCenterX(float x) {
        paddleSetCenterX(x,0);
    }
    /**
     * API utility method
     * @param x - the x value that will be assigned to the first paddle in the paddleSet 
     * @param whichPaddle - the index used to select the paddle to use out of the paddleSet
     */
    protected void paddleSetCenterX(float x, int whichPaddle) {
        paddleSet.get(whichPaddle).setCenterX(x);
        paddleSet.get(whichPaddle).clampPaddle();
    }
  
    /**
     * API utility method
     * Check if the first ball in the ballSet collided with the first paddle in the paddleSet
     * @return - true if the first ball in the ballSet collided with the first paddle in the paddleSet
     */
    public boolean ballCollidedWithPaddle() {  //ROB!! Reuse the below pattern for facade redirect and flexibility!!
        return ballCollidedWithPaddle(0,0);
    }
    
    /**
     * API utility method
     * Check if the given ball collided with the given paddle
     * @param whichBall - an index to select the ball to use out of the ballSet
     * @param whichPaddle - an index to select the paddle to use out of the paddleSet
     * @return - true if the ball specified collided with the paddle specified
     */
    public boolean ballCollidedWithPaddle(int whichBall, int whichPaddle) {
        return ballSet.get(whichBall).collided(paddleSet.get(whichPaddle));
    }

    /**
     * API utility method
     * Reflects the first ball in the ballSet about the first paddle in the paddleSet
     */
    public void ballReflectOffPaddle() {  //note the name change for consistency above with other util methods
         ballReflectOffPaddle(0,0);
    }
    
    /**
     * API utility method
     * Reflects the given ball off the given paddle
     * @param whichBall - an index to select the ball to use out of the ballSet
     * @param whichPaddle - an index to select the paddle to use out of the paddleSet
     */
    public void ballReflectOffPaddle(int whichBall, int whichPaddle) {  //note the name change for consistency above with other util methods
         paddleSet.get(whichPaddle).reflect(ballSet.get(whichBall));  //method pushes the ball out, so the better solution
    }
    /**
     * API utility method
     * Play the bouncing sound
     */
    public void ballPlayBounceSound() {
        ballSet.get(0).playBounceSound();
    }
    /**
     * API utility method
     * Check if the first ball in the ballSet is visible or not
     * @return - true if the first ball in the ballSet is visible
     */
    public boolean ballGetVisibility() {
        return ballGetVisibility(0);
    }
    /**
     * API utility method
     * Check if the given ball is visible or not
     * @param whichBall - the index used to select the ball to use out of the ballSet
     * @return - true if the ball specified is visible
     */
    public boolean ballGetVisibility(int whichBall) {
        return ballSet.get(whichBall).isVisible();
    }
    
    /**
     * API utility method
     * Spawn the given ball near the given paddle
     * @param whichBall - an index to select the ball to use out of the ballSet
     * @param whichPaddle - an index to select the paddle to use out of the paddleSet
     */
    public void ballSpawnNearPaddle(int whichBall, int whichPaddle) {
        ballSet.get(whichBall).spawn(paddleSet.get(whichPaddle));
    }
    /**
     * API utility method
     * Spawns the first ball in the ballSet near the first paddle in the paddleSet
     */
    public void ballSpawnNearPaddle() {
        ballSpawnNearPaddle(0,0);
    }
    
    /**
     * API utility method
     * Set the given image to the first paddle in the paddle set
     * @param fileName - the name of the image to set the paddle to, prefixed with "paddles/", as in "paddles/P2.png" 
     */
    public void paddleSetImage(String fileName) {
        paddleSetImage(fileName, 0);
    }
    /**
     * API utility method
     * Set the given image to the given paddle
     * @param fileName - the name of the image to set the paddle to, prefixed with "paddles/", as in "paddles/P2.png" 
     * @param whichPaddle - the index of the paddle to select out of the paddleSet
     */
    public void paddleSetImage(String fileName, int whichPaddle){
        paddleSet.get(whichPaddle).setImage(fileName);
    }

    /**
     * API utility method
     * Check if the key we're investigating is currenly being pressed
     * @param key - the key we're investigating
     * @return - true if the key we're investigating is currently being pressed
     */
    public boolean isKeyboardButtonDown(KeysEnum key) {
        return keyboard.isButtonDown(keyEventMap[key.ordinal()]);  //ordinal is like indexOf for enums->ints
    }

    
    /**
     * API utility method
     * Move the first paddle in the paddleSet to the left
     */
    public void paddleMoveLeft() {
        paddleMoveLeft(0);
    }
    /**
     * API utility method
     * Move the given paddle to the left
     * @param whichPaddle - the index used to select the paddle to use out of the paddleSet
     */
    public void paddleMoveLeft(int whichPaddle) {  //nomenclature: paddle.moveLeft()
        Paddle paddle = paddleSet.get(whichPaddle);
        paddle.moveLeft();
    }
    
    /**
     * API utility method
     * Move the first paddle in the paddleSet to the right
     */
    public void paddleMoveRight() {
        paddleMoveRight(0);
    }
    /**
     * API utility method
     * Move the given paddle to the right
     * @param whichPaddle - the index used to select the paddle to use out of the paddleSet
     */
    public void paddleMoveRight(int whichPaddle) {  //nomenclature: paddle.moveLeft()
        Paddle paddle = paddleSet.get(whichPaddle);
        paddle.moveRight();
    }
    
    /**
     * API utility method
     * @param row - the row for the target block
     * @param col - the column for the target block
     * @param type - the type of block that was at (row, col)
     */
    private void removeNeighboringBlocks(int row, int col, Block.BlockType type) {
        for (int iRow = row - 2; iRow < row + 2; iRow++) {
            if (iRow < 0 || iRow >= blockSet.getNumRows()) continue;  //skip certain cases (a non-lab IF)

            for (int iCol = col - 2; iCol < col+2; iCol++) {
                if (iCol < 0 || iCol >= blockSet.getNumColumns()) continue;  //skip certain cases (a non-lab IF)

                Block nextBlock = blockSet.getBlockAt(iRow, iCol);
                
                if (nextBlock != null && nextBlock.getType() == type) { /** SOLN */
                    blockSet.remove(nextBlock);
                }
            }
        }
    }

    /**
     * API utility method
     * Used to process all block and ball collisions for the first ball in the ballSet
     */
    protected void handleBlockBallCollision() {
        handleBlockBallCollision(0,0);
    }
    
    /**
     * API utility method
     * Used to process all block and ball collisions for the specified ball and paddle
     * The paddle here will change state if a special block was destroyed
     * 
     * @param whichBall - an index to select the ball to use out of the ballSet
     * @param whichPaddle - an index to select the paddle to use out of the paddleSet
     */
    protected void handleBlockBallCollision(int whichBall, int whichPaddle) {
        Ball ball = ballSet.get(whichBall);
        Paddle paddle = paddleSet.get(whichPaddle);
        
        
         Block block = blockSet.getCollidedBlock(ball); //find our collision target
         block.reflect(ball);                           //reverse ball direction relative to the target
        
        int row = block.getRow();
        int col = block.getColumn();
        
        if(block.getType() != Block.BlockType.CAGE_ACTIVE) 
            blockSet.remove(block); //except for CAGE blocks, remove collided block 
    
        
        ball.playBounceSound(); 
        
        switch(block.getType()) {
            case FIRE:
                removeNeighboringBlocks(row, col, Block.BlockType.FIRE);  
                paddle.startFire(); 
                break;
            
            case FREEZING:
                removeNeighboringBlocks(row, col, Block.BlockType.FREEZING);
                paddle.startIce(); //BUG: this code fails if you have called paddle.setImage("paddles/ice_paddle.png") before calling this
                
            case NORMAL:  //do nothing for a normal block   
            default:      //and do nothing here
        }
    }
}
