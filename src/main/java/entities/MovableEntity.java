package entities;

import enums.CollisionSide;
import enums.MoveDirection;
import javafx.geometry.Rectangle2D;

/**
 * Created by NoIdeas.
 */

public abstract class MovableEntity extends Sprite
{
    private float velocityX = 0.0f;
    private float velocityY = 0.0f;
    private float forceX = 0.0f;
    private float forceY = 0.0f;
    private float accelerationX = 0.0f;
    private float accelerationY = 0.0f;
    private float frictionX = 0.0f;
    private float frictionY = 0.0f;
    private MoveDirection moveDirection = MoveDirection.STOPPED;
    private CollisionSide collisionSide = CollisionSide.NONE;

    @Override
    public void update(float delay)
    {
        super.update(delay);

        this.gravity();
        this.airFriction();
        //this.groundFriction();
        this.applyForce();

        this.updateCollisionSide();
        this.updateMoveDirection();

        if (this.getCollisionSide() != CollisionSide.NONE)
        {
            switch (this.getCollisionSide())
            {
                case LEFT:
                    this.setVelocityX(0);
                    break;
                case RIGHT:
                    this.setVelocityX(0);
                    break;
                case UP:
                    this.setVelocityY(0);
                    break;
                case DOWN:
                    this.setVelocityY(0);
                    break;
            }
        }

        this.tryMove();
        this.move();

        this.checkCollision();
    }

    private void gravity()
    {
        this.addForce(0,0.5f);
    }

    private void airFriction()
    {
        float airFriction = 0.2f;

        if (this.getVelocityX() != 0)
        {
            if (this.getVelocityX() < 0)
                if (this.getVelocityX() + airFriction > 0)
                    this.setVelocityX(0);
                else
                    this.addForce(airFriction, 0);
            else
                if (this.getVelocityX() + airFriction < 0)
                    this.setVelocityX(0);
                else
                    this.addForce(-airFriction, 0);
        }
    }

    private void groundFriction()
    {
        float groundFriction = 0.3f;

        if (this.getCollisionSide() == CollisionSide.DOWN)
            if (this.getVelocityX() != 0)
            {
                if (this.getVelocityX() < 0)
                    if (this.getVelocityX() + groundFriction > 0)
                        this.setVelocityX(0);
                    else
                        this.addForce(groundFriction, 0);
                else
                    if (this.getVelocityX() + groundFriction < 0)
                        this.setVelocityX(0);
                    else
                        this.addForce(-groundFriction, 0);
            }
    }

    private void applyForce()
    {
        this.setVelocityX(this.getVelocityX() + this.getForceX());
        this.setVelocityY(this.getVelocityY() + this.getForceY());
    }

    private void updateCollisionSide()
    {
        if (this.getForceX() != 0 || this.getForceY() != 0)
        {
            if (this.getForceX() != 0)
            {
                if (this.getForceX() < 0)
                    this.setCollisionSide(this.isSideFree(CollisionSide.LEFT) ? CollisionSide.NONE : CollisionSide.LEFT);
                else
                    this.setCollisionSide(this.isSideFree(CollisionSide.RIGHT) ? CollisionSide.NONE : CollisionSide.RIGHT);
            }

            if (this.getForceY() != 0)
            {
                if (this.getForceY() < 0)
                    this.setCollisionSide(this.isSideFree(CollisionSide.UP) ? CollisionSide.NONE : CollisionSide.UP);
                else
                    this.setCollisionSide(this.isSideFree(CollisionSide.DOWN) ? CollisionSide.NONE : CollisionSide.DOWN);
            }
        }
        else if (this.getMoveDirection() != MoveDirection.STOPPED)
        {
            switch (this.getMoveDirection())
            {
                case LEFT:
                    if (!this.isSideFree(CollisionSide.LEFT))
                        this.setCollisionSide(CollisionSide.LEFT);
                    break;
                case RIGHT:
                    if (!this.isSideFree(CollisionSide.RIGHT))
                        this.setCollisionSide(CollisionSide.RIGHT);
                    break;
                case UP:
                    if (!this.isSideFree(CollisionSide.UP))
                        this.setCollisionSide(CollisionSide.UP);
                    break;
                case DOWN:
                    if (!this.isSideFree(CollisionSide.DOWN))
                        this.setCollisionSide(CollisionSide.DOWN);
                    break;
                case LEFTUP:
                    if (!this.isSideFree(CollisionSide.LEFT))
                        this.setCollisionSide(CollisionSide.LEFT);
                    if (!this.isSideFree(CollisionSide.UP))
                        this.setCollisionSide(CollisionSide.UP);
                    break;
                case LEFTDOWN:
                    if (!this.isSideFree(CollisionSide.LEFT))
                        this.setCollisionSide(CollisionSide.LEFT);
                    if (!this.isSideFree(CollisionSide.DOWN))
                        this.setCollisionSide(CollisionSide.DOWN);
                    break;
                case RIGHTUP:
                    if (!this.isSideFree(CollisionSide.RIGHT))
                        this.setCollisionSide(CollisionSide.RIGHT);
                    if (!this.isSideFree(CollisionSide.UP))
                        this.setCollisionSide(CollisionSide.UP);
                    break;
                case RIGHTDOWN:
                    if (!this.isSideFree(CollisionSide.RIGHT))
                        this.setCollisionSide(CollisionSide.RIGHT);
                    if (!this.isSideFree(CollisionSide.DOWN))
                        this.setCollisionSide(CollisionSide.DOWN);
                    break;
            }
        }
        else
            this.setCollisionSide(CollisionSide.NONE);
    }

    private void updateMoveDirection()
    {
        if (this.getCollisionSide() == CollisionSide.NONE)
        {
            if (this.getVelocityX() != 0 || this.getVelocityY() != 0)
            {
                if (this.getVelocityX() == 0 || this.getVelocityY() == 0)
                {
                    if (this.getVelocityX() != 0)
                        this.setMoveDirection(this.getVelocityX() < 0 ? MoveDirection.LEFT : MoveDirection.RIGHT);
                    else
                        this.setMoveDirection(this.getVelocityY() < 0 ? MoveDirection.UP : MoveDirection.DOWN);
                }
                else
                {
                    if (this.getVelocityX() < 0)
                        this.setMoveDirection(this.getVelocityY() < 0 ? MoveDirection.LEFTUP : MoveDirection.LEFTDOWN);
                    else
                        this.setMoveDirection(this.getVelocityY() < 0 ? MoveDirection.RIGHTUP : MoveDirection.RIGHTDOWN);
                }
            }
        }
        else
            this.setMoveDirection(MoveDirection.STOPPED);
    }

    private boolean isSideFree(CollisionSide side)
    {
        Rectangle2D collisionLine = null;
        Rectangle2D entityCollisionBox = this.getCollisionBox();

        switch (side)
        {
            case LEFT:
                collisionLine = new Rectangle2D(entityCollisionBox.getMinX() - 1, entityCollisionBox.getMinY(), 1, this.getHeight());
                break;
            case RIGHT:
                collisionLine = new Rectangle2D(entityCollisionBox.getMaxX(), entityCollisionBox.getMinY(), 1, this.getHeight());
                break;
            case UP:
                collisionLine = new Rectangle2D(entityCollisionBox.getMinX(), entityCollisionBox.getMinY() - 1, this.getWidth(), 1);
                break;
            case DOWN:
                collisionLine = new Rectangle2D(entityCollisionBox.getMinX(), entityCollisionBox.getMaxY(), this.getWidth(), 1);
                break;
            case NONE:
                collisionLine = this.getCollisionBox();
                break;
        }

        for (Sprite sprite : gameBoard.rigidBodySprites)
        {
            if (sprite.getCollisionBox().intersects(collisionLine))
            {
                return false;
            }
        }

        return true;
    }

    private void tryMove()
    {
        float absVelocityX = Math.abs(this.getVelocityX());
        float absVelocityY = Math.abs(this.getVelocityY());
        Rectangle2D nextStepRect = null;
        Rectangle2D collisionBox = this.getCollisionBox();

        switch (this.getMoveDirection())
        {
            case LEFT:
                nextStepRect = new Rectangle2D(collisionBox.getMinX() - absVelocityX, collisionBox.getMinY(), absVelocityX, this.getHeight());
                break;
            case RIGHT:
                nextStepRect = new Rectangle2D(collisionBox.getMinX() + collisionBox.getWidth(), collisionBox.getMinY(), absVelocityX, this.getHeight());
                break;
            case UP:
                nextStepRect = new Rectangle2D(collisionBox.getMinX(), collisionBox.getMinY() - absVelocityY, this.getWidth(), absVelocityY);
                break;
            case DOWN:
                nextStepRect = new Rectangle2D(collisionBox.getMinX(), collisionBox.getMinY() + collisionBox.getHeight(), this.getWidth(), absVelocityY);
                break;
        }

        if (nextStepRect != null)
        {
            float tempVelocityX = this.getVelocityX();
            float tempVelocityY = this.getVelocityY();
            for (Sprite sprite : gameBoard.rigidBodySprites)
            {
                Rectangle2D spriteCollisionBox = sprite.getCollisionBox();
                if (spriteCollisionBox.intersects(nextStepRect))
                {
                    switch (this.getMoveDirection())
                    {
                        case LEFT:
                            tempVelocityX = (float) spriteCollisionBox.getMaxX() - (float) collisionBox.getMinX();
                            break;
                        case RIGHT:
                            tempVelocityX = (float) spriteCollisionBox.getMinX() - (float) collisionBox.getMaxX();
                            break;
                        case UP:
                            tempVelocityY = (float) spriteCollisionBox.getMaxY() - (float) collisionBox.getMinY();
                            break;
                        case DOWN:
                            tempVelocityY = (float) spriteCollisionBox.getMinY() - (float) collisionBox.getMaxY();
                            break;
                    }
                    this.setVelocity(tempVelocityX, tempVelocityY);
                }
            }
        }
    }

    private void move()
    {
        super.setPositionX(super.getPositionX() + this.getVelocityX());
        super.setPositionY(super.getPositionY() + this.getVelocityY());
    }

    private void checkCollision()
    {
        Rectangle2D collisionBox = this.getCollisionBox();
        float centerX = super.getPositionX() + super.getWidth() / 2;
        float entityCenterY = super.getPositionY() + super.getHeight() / 2;

        for (Sprite obstacle : gameBoard.rigidBodySprites)
        {
            Rectangle2D obstacleCollisionBox = obstacle.getCollisionBox();
            float obstacleCenterX = obstacle.getPositionX() + obstacle.getWidth() / 2;
            float obstacleCenterY = obstacle.getPositionY() + obstacle.getHeight() / 2;

            if (obstacleCollisionBox.intersects(collisionBox) && obstacle != this)
            {
                Rectangle2D collisionIntersection = intersect(collisionBox, obstacleCollisionBox);
                float diffX = (obstacleCenterX - centerX);
                float diffY = (obstacleCenterY - entityCenterY);

                if (collisionIntersection.getWidth() < collisionIntersection.getHeight())
                {
                    if (diffX < 0)
                        super.setPositionX(super.getPositionX() + (float) collisionIntersection.getWidth());
                    else
                        super.setPositionX(super.getPositionX() - (float) collisionIntersection.getWidth());
                }
                else
                {
                    if (diffY < 0)
                        super.setPositionY(super.getPositionY() + (float) collisionIntersection.getHeight());
                    else
                        super.setPositionY(super.getPositionY() - (float) collisionIntersection.getHeight());
                }
            }
        }
    }

    public void setVelocity(float velocityX, float velocityY)
    {
        this.setVelocityX(velocityX);
        this.setVelocityY(velocityY);
    }

    public float getVelocityX()
    {
        return this.velocityX;
    }

    public void setVelocityX(float velocityX)
    {
        this.velocityX = velocityX;
    }

    public float getVelocityY()
    {
        return this.velocityY;
    }

    public void setVelocityY(float velocityY)
    {
        this.velocityY = velocityY;
    }

    public void addForce(float forceX, float forceY)
    {
        this.forceX += forceX;
        this.forceY += forceY;
    }

    public void resetForce()
    {
        this.forceX = 0;
        this.forceY = 0;
    }

    public float getForceX()
    {
        return this.forceX;
    }

    public float getForceY()
    {
        return this.forceY;
    }

    public float getAccelerationX()
    {
        return accelerationX;
    }

    public void setAccelerationX(float accelerationX)
    {
        this.accelerationX = accelerationX;
    }

    public float getAccelerationY()
    {
        return accelerationY;
    }

    public void setAccelerationY(float accelerationY)
    {
        this.accelerationY = accelerationY;
    }

    public MoveDirection getMoveDirection()
    {
        return this.moveDirection;
    }

    public void setMoveDirection(MoveDirection moveDirection)
    {
        this.moveDirection = moveDirection;
    }

    public CollisionSide getCollisionSide()
    {
        return this.collisionSide;
    }

    public void setCollisionSide(CollisionSide collisionSide)
    {
        this.collisionSide = collisionSide;
    }
}
