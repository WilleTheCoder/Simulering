
public class Box{
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
            // System.out.println("Two people already talking");
        }

        status_update();
    }
    public void add(Minion m) {
        if(m1 == null){
            m1 = m;
        } else if(m2 == null){
            m2 = m;
        }        
        status_update();
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
        } else if(m == m2){
            return m1;
        } else{
            System.out.println("wtfffffffffffffffffffffffffffffff");
            return null;
        }
    }

}
