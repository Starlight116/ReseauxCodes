
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


/**TP 1 - Reseaux : 
 * Programme d'interface pour créer des graphs de transmission de bandes de base
 * @author Camille Protat
 * @version 1
 */
public class Fenetre extends JFrame {
	
	//Tout les éléments mis ici en variables d'instances 
	//sont utilisés dans plusieurs fonctions dont GPanel
	
	//Les boutons radio permettent de choisir le code de transmission
	private JRadioButton nrz,manchester,differentiel,miller;
	
	//les boutons radios seront groupés dans ce groupe b pour garder leur action de bouton radio.
	//si un est selectionné, les autres ne le seront plus.
	private ButtonGroup b;
	
	//le champ de texte permet de récupérer le code binaire
	private JTextField field;
	
	//Le bouton Valider va permettre d'associer la génération du graphe au click sur celui-ci
	private JButton valider;
	
	// GPanel est une classe fille de JPanel qui dessine les graphs.
	private GPanel inside;
	
	
	
	/**
	 * Constructeur de la Fenêtre de base.
	 */
	public Fenetre() {
		super("Transmission en bande de base"); //titre
		
		//initialisation des boutons
		nrz = new JRadioButton("NRZ");
		manchester = new JRadioButton("Manchester");
		differentiel = new JRadioButton("Manchester Differentiel");
		miller = new JRadioButton("Miller");
		
		//init est la fonction de base qui ajoute les éléments à la fenêtre.
		init();
	
		setSize(600,400);//taille
		setLocationRelativeTo(null);//affiche la fenêtre au centre de l'écran 
		setDefaultCloseOperation(EXIT_ON_CLOSE);//permet de fermer le programme
		setVisible(true);//pour afficher la fenêtre à l'écran
	}
	
	/**
	 * init() appelle creerMenu() et creerGraph()
	 */
	private void init(){
		//on utilise un BorderLayout pour le cadre
		JPanel view = new JPanel();
		view.setLayout(new BorderLayout());
		
		//le ContentPane de la fenêtre est maintenant view.
		setContentPane(view);
		
		view.add(creerMenu(),BorderLayout.NORTH);
		view.add(creerGraph(),BorderLayout.CENTER);
		
		
	}
	
	/**
	 * Construit le menu en haut en appelant 2 méthode qui construisent les parties gauches et droites.
	 * @return le JPanel contenant le menu
	 */
	private JPanel creerMenu() {
		JPanel menu = new JPanel();
		//on a un FlowLayout par defaut ce qui centree le menu dans la fenêtre
		
		menu.add(creerMenuGauche());
		menu.add(creerMenuDroite());
		
		return menu;
	}
	
	/**
	 * Construit la partie droite du menu qui contient le
	 * champ pour le code binaire et le bouton de génération du code
	 * @return le JPanel contenant le menu de droite
	 */
	private JPanel creerMenuDroite() {
		JPanel pan = new JPanel();
		BoxLayout box = new BoxLayout(pan,BoxLayout.Y_AXIS);
		pan.setLayout(box);
		
		
		JLabel text = new JLabel("Code Binaire :");
		field = new JTextField(10);
		valider = new JButton("générer Trame");
		
		
		pan.add(text);
		pan.add(field);
		pan.add(valider);
		
		return pan;
	}
	/**
	 * Crée la partie gauche du menu qui contient les 4 boutons radio
	 * permettant de choisir le type de code pour la trame.
	 * @return le JPanel contenant le menu de gauche.
	 */
	private JPanel creerMenuGauche() {
		JPanel pan = new JPanel();
		BoxLayout box = new BoxLayout(pan,BoxLayout.Y_AXIS);
		pan.setLayout(box);
		
		
		//on fait le groupe de bouton et on ajoute nos 4 boutons dedans, ce qui permet de les synchroniser
		b = new ButtonGroup();
		
		b.add(nrz);
		b.add(manchester);
		b.add(differentiel);
		b.add(miller);
		
		pan.add(nrz);
		pan.add(manchester);
		pan.add(differentiel);
		pan.add(miller);
		
		return pan;
		
	}
	
	/**
	 * Permet de créer la partie dessin de la fenêtre avec la trame et une barre de defilement
	 * @return le JPanel contenant la trame.
	 */
	private JPanel creerGraph() {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		
		inside = new GPanel();
		
		//Le JscrollPane permet d'ajouter une barre de défilement (ici horizontale) ce qui permet 
		//de ne pas limiter la taille de notre trame à celle de la fenêtre
		JScrollPane scroller = new JScrollPane(inside,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		//On met l'action listener du bouton de validation ici pour être sur que inside est bien initialisé quand on clique sur le bouton.
		valider.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//on a un dialogue d'erreur si on appuie sur le bouton et qu'aucun bouton radio n'est selectionné
				if(b.getSelection()==null) {
					JOptionPane.showMessageDialog(Fenetre.this,"Veuillez choisir l'encodage de la trame !!","erreur",JOptionPane.ERROR_MESSAGE);
				}else{
					//on vérifie que le texte tapé est en binaire, si il est null ou si on a tapé autre chose on a un dialogue d'erreur
					if(!Fenetre.estBinaire(field.getText())) {
						JOptionPane.showMessageDialog(Fenetre.this,"Ecrivez un code Binaire avant de Valider !","erreur",JOptionPane.ERROR_MESSAGE);
					}else {
						//on vérifie bien qu'on a selectionné un bouton ET que le texte n'est pas null avant de générer la trame
						if(b.getSelection()!=null && field.getText()!=null) {
							inside.updateGraph(field.getText());
						}
					}
				}
			}
		});
		
		p.add(scroller);
		
		return p;
	}
	
	/**
	 * Vérifie qu'une chaine de caractère est en binaire
	 * @param chaine : String - la chaine de caractère a vérifier
	 * @return true si la chaine est en binaire, sinon false.
	 */
	public static boolean estBinaire(String chaine) {
        // Utilisation d'une expression régulière pour vérifier la présence de seulement 0 et 1
        return chaine.matches("[01]+");
    }
	
	/**
	 * GPanel est un JPanel personnalisé qui sert a dessiner
	 * 
	 */
	class GPanel extends JPanel {
		//On défini des couleurs pour pas avoir a les retaper
		Color white = new Color(255,255,255);
		Color black = new Color(0,0,0);
		Color red = new Color(255,0,0);
		int defaultWidth = 30;
		int defaultAdd = 25;
		int defaultMult = 50;
		
		int size; // La taille de la zone blanche du dessin
		String binaire; //La chaine en binaire qui permet de générer la trame
		
		boolean bas;//sens de départ de la trame pour les types qui se fichent du sens, par défaut on commence a -nV 
		
		public GPanel() {
			binaire="";
			size = defaultWidth;
			bas=true;
			setPreferredSize(new Dimension(size, getHeight()));
			//getHeight() permet de faire en sorte que GPanel prenne toute la place qu'on veut bien lui donner en hauteur
	    }

	    /**
	     *
	     */
	    @Override
	    public void paintComponent(Graphics g) {
	    	super.paintComponent(g);
	    	
	    	
	        g.setColor(white);
	        g.fillRect(0, 0, size, getHeight());
	        g.setColor(black);
	        
	        //on dessine les lignes et le texte de l'entête de la trame
	        drawDots(g,'V',0,getHeight(),defaultWidth);
	        g.drawString("0V",5,2*getHeight()/4);
	        
	        drawDots(g,'H',defaultAdd,size,2*getHeight()/4-5);
	        g.drawString("nV",5,getHeight()/4);
	        
	        drawDots(g,'H',defaultAdd,size,getHeight()/4-5);
	        g.drawString("-nV",4,3*getHeight()/4);
	        
	        drawDots(g,'H',defaultAdd,size,3*getHeight()/4-5);
	        
	        //si la chaine est vide on aura un tableau vide, donc le programme ne tracera pas ce qui est dans la boucle suivante
        	char[] tab = binaire.toCharArray();
        	
        	for(int i = 0;i<tab.length; i++) {
        		
        		//on écrit les chiffres au dessus
        		if(tab[i]=='0') {
        			g.drawString("0",i*defaultMult+defaultMult, getHeight()/6);
        		} else {
        			g.drawString("1",i*defaultMult+defaultMult, getHeight()/6);
        		}
        		//on dessine les lignes des graduations verticales de la trame
	        	drawDots(g,'V',0,getHeight(),i*defaultMult+(defaultWidth+defaultMult));
	        	
	        	//En fonction de ce qui est selectionné on trace la trame.
	        	if(nrz.isSelected()) {
	        		drawNRZ(g,tab,i);
	        	}else if(manchester.isSelected()){
	        		drawManchester(g,tab,i);
	        	}else if(differentiel.isSelected()) {
	        		drawDiff(g,tab,i);
	        	}else {
	        		drawMiller(g,tab,i);
	        	}
	        	
	        }
	        
	    }
	    
	    
	    /**
	     * Trace la trame avec le code de Miller
	     * @param g : Graphics 
	     * @param tab : char[] - tableau de caratère binaires
	     * @param i : position courante dans le tableau
	     */
	    private void drawMiller(Graphics g, char[] tab, int i) {
	    	g.setColor(red);
	    	
	    	if(tab[i]=='1') {
	    		//on trace la ligne verticale au milieu
	    		drawLineV(g,getHeight()/4,i*defaultMult+defaultWidth+defaultAdd);
	    		//les 2 horizontales sont trcés en fonction du sens.
				if(bas) {
		    		drawLineH(g,3*getHeight()/4-5,i*defaultMult+defaultWidth,defaultAdd);
		    		drawLineH(g,getHeight()/4-5,(i*defaultMult+defaultWidth)+defaultAdd,defaultAdd);
		    	}else {
		    		drawLineH(g,3*getHeight()/4-5,(i*defaultMult+defaultWidth)+defaultAdd,defaultAdd);
		    		drawLineH(g,getHeight()/4-5,i*defaultMult+defaultWidth,defaultAdd);
		    	}
				//on change de sens après avoir tracé
		    	bas=!bas;
		    	
			}else {
				
				//on dessine la ligne horizontale sur toute l'horloge
				if(bas) {
					drawLineH(g,3*getHeight()/4-5,i*defaultMult+defaultWidth,defaultMult);
				}else {
					drawLineH(g,getHeight()/4-5,i*defaultMult+defaultWidth,defaultMult);
				}
				
				if(i<tab.length-1 && tab[i+1]=='0')bas=!bas; //on change de sens quand on a deux 0 de suite
				
				if(i>0 && tab[i-1]=='0') {
					//on dessine la ligne verticale seulement si on a un 0 avant
					drawLineV(g,getHeight()/4,i*defaultMult+defaultWidth);
		    	}
			}
			
			g.setColor(black);
		}

		/**
		 Trace la trame avec le code de Manchester Différentiel
	     * @param g : Graphics 
	     * @param tab : char[] - tableau de caratère binaires
	     * @param i : position courante dans le tableau
		 */
		private void drawDiff(Graphics g, char[] tab, int i) {
	    	g.setColor(red);
	    	
			if(tab[i]=='0') {
				//ligne verticale en début d'horloge à chaque 0
	    		drawLineV(g,getHeight()/4,i*defaultMult+defaultWidth);
	    		bas=!bas;
	    	}
			
			//dessin de la transition entre nV et -nV
			drawLineV(g,getHeight()/4,i*defaultMult+defaultWidth+defaultAdd);
	    	if(bas) {
	    		drawLineH(g,3*(getHeight()/4)-5,i*defaultMult+defaultWidth,defaultAdd);
	    		drawLineH(g,(getHeight()/4)-5,(i*defaultMult+defaultWidth)+defaultAdd,defaultAdd);
	    		
	    	}else {
	    		drawLineH(g,3*(getHeight()/4)-5,(i*defaultMult+defaultWidth)+defaultAdd,defaultAdd);
	    		drawLineH(g,(getHeight()/4)-5,i*defaultMult+defaultWidth,defaultAdd);
	    		
	    	}
	    	bas=!bas;
	    	
	    	
			g.setColor(black);
	    }
	    
	    /**
	     Trace la trame avec le code de Manchester
	     * @param g : Graphics 
	     * @param tab : char[] - tableau de caratère binaires
	     * @param i : position courante dans le tableau
	     */
	    private void drawManchester(Graphics g,char[] tab, int i) {
	    	g.setColor(red);
	    	
	    	//on choisi le sens du dessin en fonction de si on a 0 ou 1
	    	switch(tab[i]) {
	    		case '0':
	    			drawLineH(g,3*(getHeight()/4)-5,i*defaultMult+defaultWidth,defaultAdd);
	    			drawLineH(g,(getHeight()/4)-5,(i*defaultMult+defaultWidth)+defaultAdd,defaultAdd);
	    			break;
	    		case '1':
	    			drawLineH(g,(getHeight()/4)-5,i*defaultMult+defaultWidth,defaultAdd);
	    			drawLineH(g,3*(getHeight()/4)-5,(i*defaultMult+defaultWidth)+defaultAdd,defaultAdd);
	    			
	    			break;
    		}
	    	//Le dessin de la ligne verticale en milieu d'horloge est toujours le même
	    	drawLineV(g,getHeight()/4,i*defaultMult+defaultWidth+defaultAdd);
	    	
    		//le dessin des lignes verticales en debut d'horloge ne se fait que si on a 2 bits égaux à la suite
    		if(i>0 && tab[i-1]==tab[i]) {
    			drawLineV(g,getHeight()/4,i*defaultMult+defaultWidth);
    		}
    		g.setColor(black);
	    }
	    
	    /**
	     Trace la trame avec le code NRZ
	     * @param g : Graphics 
	     * @param tab : char[] - tableau de caratère binaires
	     * @param i : position courante dans le tableau
	     */
	    private void drawNRZ(Graphics g,char[] tab, int i) {
	    	g.setColor(red);
	    	
	    	//on dessine la ligne a nZ en cas de 1 et a -nz en cas de 0
    		switch(tab[i]) {
    		case '0':
    			drawLineH(g,3*(getHeight()/4)-5,i*defaultMult+defaultWidth,defaultMult);
    			break;
    		case '1':
    			drawLineH(g,(getHeight()/4)-5,i*defaultMult+defaultWidth,defaultMult);
    			break;
    		}
    		
    		//la ligne verticale est toujours la même et dessinée seulement quand on a un bit différent avant le courant.
    		if(i>0 && tab[i-1]!=tab[i]) {
    			drawLineV(g,(getHeight()/4),i*defaultMult+defaultWidth);
    		}
    		g.setColor(black);
	    }
	    
	    /**
	     * Dessine une ligne Horizontale.
	     * Comme la hauteur évolue en fonction de la fenêtre 
	     * on a des paramètres pour les 2 positions de la ligne (en X et Y)
	     * @param g : Graphics
	     * @param posY : int 
	     * @param posX : int
	     * @param lgt : int - la longueur de la ligne
	     */
	    private void drawLineH(Graphics g, int posY, int posX, int lgt) {
	    	g.drawLine(posX, posY, posX+lgt, posY);
	    }
	    /**
	     * Dessine une ligne verticale entre -nV et nV
	     * Comme la hauteur évolue en fonction de la fenêtre 
	     * on a des paramètres pour les 2 positions de la ligne (en X et Y)
	     * @param g : Graphics
	     * @param posY : int
	     * @param posX : int
	     */
	    private void drawLineV(Graphics g, int posY, int posX) {
	    	g.drawLine(posX,posY-5,posX,3*posY-5);
	    }
	    
	    /**
	     * Dessiner des lignes en pointillés
	     * @param g : Graphics
	     * @param dir : char - la direction (vertical ou horizontal)
	     * @param posDeb : int - la position ou commence la ligne
	     * @param posFin : int - la position ou fini la ligne
	     * @param pos : la position de l'autre paramètre de la ligne
	     */
	    private void drawDots(Graphics g, char dir, int posDeb, int posFin, int pos) {
	    	
	    	if(dir == 'H') {
	    		for(int i=posDeb; i<posFin; i+=10) {
		        	g.drawLine(i, pos, i+5, pos);
		        }
	    		
	    	} else if (dir=='V') {
	    		for(int i=posDeb; i<posFin; i+=10) {
		        	g.drawLine(pos, i, pos, i+5);
		        }
	    	}
	    }

	    /**
	     * Redessiner la Trame
	     * @param string - le nouveau code binaire
	     */
	    public void updateGraph(String string) {
            binaire = string;
            size = (binaire.length())*defaultMult+defaultWidth; //on augmente la taille pour avoir un espace blanc propotionnel au nombre de bits
            bas=true;
            
            removeAll(); //on efface l'ancien graphe
            
            setPreferredSize(new Dimension(size, getHeight()));
            
            revalidate(); //permet d'éviter un conflit si jamais la nouvelle entrée n'est pas valide
            repaint();  // Redessine le graphique avec la nouvelle taille
        }
	    
	    
	}
	
	
	// MAIN 
	public static void main(String[] args){
		SwingUtilities.invokeLater(Fenetre::new);
	}
}
