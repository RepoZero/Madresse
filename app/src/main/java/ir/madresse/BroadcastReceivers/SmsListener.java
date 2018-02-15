package ir.madresse.BroadcastReceivers;


public interface SmsListener {

    public void messageReceived(String meesageSender, String messageText);

}
