
public class Box extends Global {
    protected int status;
    protected Minion m1;
    protected Minion m2;

    Box() {
        this.status = 0;
    }

    public void remove(Minion m) {
        if(m.equals(m1)){
            m1 = null;
        } else if (m.equals(m2)){
            m2 = null;
        } else{
            // System.out.println("why");
        }

        status_update();
    }
    public int add(Minion m) {
        if(m1 == null){
            m1 = m;
        } else if(m2 == null){
            m2 = m;
        } else{
            return 3;
        }
        
        status_update();
        return -1;
    }

    private void status_update(){
        if (m1 == null ^ m2 == null){
            status = 1;
        } else if(m1 == null & m2 == null){
            status = 0;
        } else if(m1 != null & m2 != null){
            status = 2;
        }
    }

    public Minion get_other(Minion m){
        if(m == m1){
            return m2;
        } else{
            return m1;
        }
    }

}
