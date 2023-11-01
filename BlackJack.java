package BlackJackGame;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.awt.event.*;
import java.util.ArrayList;


public class BlackJack {

    private class Card{
        String value;
        String suit;


        Card(String value, String suit){
            this.value = value;
            this.suit = suit;
        }

        public String toString(){

            return "Value: "+this.value+"\nSuit: "+this.suit;
        }




    }



    
}
