import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.util.Random;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class BlackJack {

    private class Card{

        private String value;
        private String suit;

        Card(String value, String suit){
            this.value = value;
            this.suit = suit;
        }

        public String toString(){
            return this.value + "-" + this.suit;
        }

        public int getValue() {
            if("AJQK".contains(this.value)){

                if(this.value == "A"){
                    return 11;
                }
                return 10;
            }

            return Integer.parseInt(this.value);
        }

        public boolean isAce() {
            return this.value == "A";
        }

        public String getImagePath(){
            return "./assets/cards/"+ toString() +".png";
        }
    }

    ArrayList<Card> deck;
    Random random = new Random();

    //For the dealer
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;

    //For the Player
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    //For the window
    int boardWidth = 1000;
    int boardHeight = 800;

    //card
    int cardWidth = 150;
    int cardHeight = 210;
    

    JFrame frame  = new JFrame("Black Jack");

    JPanel gamePanel = new JPanel(){
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            // Draw the background
            ImageIcon bgIcon = new ImageIcon(this.getClass().getResource("assets/gradient1000x800.jpg"));
            Image bgImage = bgIcon.getImage();
            g.drawImage(bgImage, 0, 0, boardWidth, boardHeight, null);


            try{
                //Draw hidden card
                Image hiddenCardImage = new ImageIcon(getClass().getResource("assets/card-back2.png")).getImage();

                if(!stayButton.isEnabled() || !hitButton.isEnabled()){
                    hiddenCardImage = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();

                    Integer dispIntegerDealer = dealerSum;
                    String dispDealerScore =  "DEALER SCORE: " + dispIntegerDealer.toString();

                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Courier", Font.BOLD, 40));
                    g.drawString(dispDealerScore, 600, 300);
                }

                g.drawImage(hiddenCardImage, 50, 50, cardWidth, cardHeight, null);

                for(int i = 0 ; i  < dealerHand.size(); i++){
                    Card card = dealerHand.get(i);
                    Image CardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(CardImage, cardWidth + 60 + (cardWidth + 10) *i, 50, cardWidth, cardHeight, null);
                    
                }

                //Draw Players Hand
                for(int i = 0 ; i < playerHand.size() ; i ++){
                    Card card = playerHand.get(i);
                    Image CardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(CardImage, 50 + (cardWidth + 10) *i, 500, cardWidth, cardHeight, null);
                }

                dealerSum = checkDealerScore();
                playerSum = checkScore();

                Integer dispIntegerPlayer = playerSum;
                String dispPlayerScore =  "PLAYER SCORE: " + dispIntegerPlayer.toString();

                
                g.setColor(Color.WHITE);
                g.setFont(new Font("Courier", Font.BOLD, 40));
                g.drawString(dispPlayerScore, 600, 500);
                

                //Draw final message
                if (!stayButton.isEnabled() || !hitButton.isEnabled()) {
                    dealerSum = checkDealerScore();
                    playerSum = checkScore();

                    String message  = "";
                    if(playerSum>21){
                        message ="BUST!";
                    }
                    else if(dealerSum>21){
                        message="You Win";
                    }
                    else if (playerSum == dealerSum) {
                        message = "Tie!";
                    }
                    else if (playerSum > dealerSum) {
                        message = "You Win!";
                    }
                    else if (playerSum < dealerSum) {
                        message = "You Lose!";
                    }

                    g.setFont(new Font("Arial", Font.PLAIN, 30));
                    g.setColor(Color.white);
                    g.drawString(message, 800, 450);

                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }


        }

    };

    JPanel buttonPanel = new JPanel();

    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");
    JButton restartButton = new JButton("Restart");
    //ImageIcon bgIcon = new ImageIcon(this.getClass().getResource("assets/gradient1000x800.jpg"));

    BlackJack(){
        startGame();

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setBounds(0, 0, boardWidth, boardHeight);
       
        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBounds(0, 0, boardWidth, boardHeight);
        
        //frame.setContentPane(bgImage); 
        //System.out.println(this.getClass().getResource("assets/gradient1000x800.jpg"));

        //hit and stay button
        hitButton.setFocusCycleRoot(false);
        hitButton.setForeground(new Color(0, 87, 12));
        stayButton.setForeground(new Color(0,87,12));
        
      
        stayButton.setFocusCycleRoot(false);
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(hitButton);
        buttonPanel.add(stayButton);
        buttonPanel.add(restartButton);
        
        //Add panels to frame
        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);


        hitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e){
                Card card = deck.remove(deck.size()-1);
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1: 0;
                playerHand.add(card); 
                
                if(checkScore() > 21){
                    hitButton.setEnabled(false);
                }

                gamePanel.repaint(); 
            }

            
        });

        stayButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                hitButton.setEnabled(false);
                stayButton.setEnabled(false); 

                while(dealerSum < 17){
                    Card card = deck.remove(deck.size()-1);
                    dealerSum += card.getValue();
                    dealerAceCount += card.isAce() ? 1: 0;
                    dealerHand.add(card); 
                }
                gamePanel.repaint();


            }
        });

        restartButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // Reset game state
                startGame();
                hitButton.setEnabled(true);
                stayButton.setEnabled(true);
                gamePanel.repaint();
            }

        });

    }

    public void startGame(){
        //Deck
        buildDeck();
        shuffleDeck();

        //Dealer
        dealerHand = new ArrayList<Card>();
        dealerSum = 0 ;
        dealerAceCount =0;

        hiddenCard =  deck.remove(deck.size()-1);
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;

        Card card = deck.remove(deck.size()-1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);

        System.out.println("DEALER:");
        System.out.println(hiddenCard);
        System.out.println(dealerHand);
        System.out.println(dealerSum);
        System.out.println(dealerAceCount);

        //Player
        playerHand = new ArrayList<Card>();
        playerSum=0;
        playerAceCount=0;

        for(int i = 2 ; i > 0 ; i--){
            card = deck.remove(deck.size()-1);
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0 ;
            playerHand.add(card);
        }

        System.out.println("PLAYER:");
        System.out.println(playerHand);
        System.out.println(playerSum);
        System.out.println(playerAceCount);
    }

    public void buildDeck() {

        deck = new ArrayList<Card>();

        String[] values = {"A", "2" , "3" , "4" , "5", "6" , "7", "8" , "9" , "10" , "J", "Q" , "K"};
        String[] suits  = {"C","D", "H", "S"};

        for(int i = 0 ; i < values.length ; i++){
            for (int j=0;j<suits.length;j++) {
                Card card = new Card(values[i], suits[j]);
                deck.add(card);
            }
        }

        System.out.println("BUILD DECK: ");
        System.out.println(deck);
    }

    public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
            new BlackJack();
        });

	}

    public void shuffleDeck(){

        for(int i = 0 ; i < deck.size() ; i++){
            int f = random.nextInt(deck.size());
            Card curr = deck.get(i);
            Card ranCard = deck.get(f);
            deck.set(i, ranCard);
            deck.set(f, curr);
        }
        System.out.println("AFTER SHUFFLE: ");
        System.out.println(deck);
    }

    public int checkScore(){

        while(playerAceCount > 1 && playerSum > 21){

            playerSum -=10;
            playerAceCount -=1;
        }
        return playerSum;
    }

    public int checkDealerScore(){

        while(dealerAceCount > 1 && dealerSum > 21){

            dealerSum -=10;
            dealerAceCount -=1;
        }
        return dealerSum;
    }

}
