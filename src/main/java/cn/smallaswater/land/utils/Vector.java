package cn.smallaswater.land.utils;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 若水
 */
public class Vector implements Cloneable{

    public Level level;

    private int startX;

    private int endX;

    private int startY;

    private int endY;

    private int startZ;

    private int endZ;
    public Vector(Level level, int startX, int endX, int startY, int endY, int startZ, int endZ) {
        this.level = level;
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.startZ = startZ;
        this.endZ = endZ;

    }



    public Vector(Position pos1, Position pos2) {
        this.level = pos1.getLevel();
        this.startX = (int) pos1.getX();
        this.endX = (int) pos2.getX();
        this.startY = (int) pos1.getY();
        this.endY = (int) pos2.getY();
        this.startZ = (int) pos1.getZ();
        this.endZ = (int) pos2.getZ();
    }

    public LinkedHashMap<String,Object> getConfig(){
        LinkedHashMap<String,Object> obj = new LinkedHashMap<>();
        obj.put("level",level.getFolderName());
        obj.put("startX",startX);
        obj.put("endX",endX);
        obj.put("startY",startY);
        obj.put("endY",endY);
        obj.put("startZ",startZ);
        obj.put("endZ",endZ);
        return obj;
    }


    /**
     * 计算领地大小
     * */
    public int size(){
        Vector vector = this.clone();
        int line = Math.abs(vector.endX - vector.startX) + 1;
        int k = Math.abs(vector.endZ - vector.startZ) + 1;
        int h = Math.abs(vector.endY - vector.startY) + 1;

        return Math.abs(line * k * h);

    }

    public Vector deSort(){
        int temp;
        if(this.startX < this.endX){
            temp = this.endX;
            this.endX = this.startX;
            this.startX = temp;
        }

        if(this.startY < this.endY){
            temp = this.endY;
            this.endY = this.startY;
            this.startY = temp;
        }

        if(this.startZ < this.endZ){
            temp = this.endZ;
            this.endZ = this.startZ;
            this.startZ = temp;
        }
        return this;
    }

    public Vector sort(){
        int temp;
        if(this.startX > this.endX){
            temp = this.endX;
            this.endX = this.startX;
            this.startX = temp;
        }

        if(this.startY > this.endY){
            temp = this.endY;
            this.endY = this.startY;
            this.startY = temp;
        }

        if(this.startZ > this.endZ){
            temp = this.endZ;
            this.endZ = this.startZ;
            this.startZ = temp;
        }
        return this;
    }

    public Position getPos1(){
        return new Position(this.startX, this.startY, this.startZ,level);
    }

    public Position getPos2(){
        return new Position(this.endX, this.endY, this.endZ,level);
    }


    public void addStartX(int x){
        startX += x;
    }
    public void addStartY(int y){
        startY += y;
    }
    public void addStartZ(int z){
        startZ += z;
    }
    public void addEndX(int x){
        endX += x;
    }
    public void addEndY(int y){
        endY += y;
    }
    public void addEndZ(int z){
        endZ += z;
    }

    public Level getLevel() {
        return level;
    }

    public int getStartX() {
        return startX;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public int getEndZ() {
        return endZ;
    }

    public int getStartY() {
        return startY;
    }

    public int getStartZ() {
        return startZ;
    }


    @Override
    public String toString() {
        return "startX: "+startX+" endX: "+endX+" startY: "+startY+" endY: "+endY+" startZ "+startZ+" endZ "+endZ;
    }

    @Override
    public Vector clone() {
        try{
            return (Vector) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Vector getVectorByMap(Map map){
        String levelName = map.get("level").toString();
        Level level = Server.getInstance().getLevelByName(levelName);
        int sx ,sy ,sz ,ex ,ey ,ez ;
        sx = (int) map.get("startX");
        ex = (int) map.get("endX");
        sy = (int) map.get("startY");
        ey = (int) map.get("endY");
        sz = (int) map.get("startZ");
        ez = (int) map.get("endZ");
        if(level == null){
            if(Server.getInstance().loadLevel(levelName)){
                level = Server.getInstance().getLevelByName(levelName);
            }
        }
        return new Vector(level,sx,ex,sy,ey,sz,ez);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj instanceof Vector){
            return (((Vector) obj).getLevel().getFolderName().equalsIgnoreCase(level.getFolderName())
                    && startX == ((Vector) obj).startX
                    && startZ == ((Vector) obj).startZ
                    && startY == ((Vector) obj).startY
                    && endX == ((Vector) obj).endX
                    && endY == ((Vector) obj).endY
                    && endZ == ((Vector) obj).endZ);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
