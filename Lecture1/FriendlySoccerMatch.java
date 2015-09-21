package pcss.lecture01;

import java.util.Random;
import java.util.Scanner;

public class FriendlySoccerMatch implements FriendlyMatch {
	Scanner sc = new Scanner(System.in);
	private String nameHomeTeam;
	private String nameGuestTeam;
	private int pointsHome;
	private int pointsGuest;
	private boolean halfTimeHappened = false;

	// Constructor
	public FriendlySoccerMatch(){
		pointsHome = 0;
		pointsGuest = 0;
	}
	
	@Override
	public String getHomeTeam() {
		return nameHomeTeam;
	}

	@Override
	public String getGuestTeam() {
		return nameGuestTeam;
	}

	@Override
	public int getHomePoints() {
		return pointsHome;
	}

	@Override
	public int getGuestPoints() {
		return pointsGuest;
	}

	@Override
	public String getResultText() {
		return "The friendly game ends with		\n\n"+nameHomeTeam+" - "+nameGuestTeam +" "+pointsHome+":"+pointsGuest+".";
	}
	
	public void startGame(Team t1, Team t2){
		nameHomeTeam = t1.getName();
		nameGuestTeam = t2.getName();
		pointsHome = 0;
		pointsGuest = 0;

		// now the game can begin; we have to create for the 
		// 90 minutes + extra time the different actions 
		Random r = new Random();
		boolean gameruns = true;
		int gameduration = 90 + r.nextInt(5);
		int time = 1;
		int nextAction = 0;
		
		// while the game runs, goals can be scored
		while (gameruns){
			nextAction = r.nextInt(15)+1;
			// Is the game over?
			if ((time + nextAction > gameduration) || (time > gameduration)){
				gameruns = false;
				break;
			}
			//*******************************************
			// A new action can take place ...
			time = time + nextAction;
			
			//INTERACTIVE! Switch-out can happen between 40 and 55 minutes, only once.
			if(time > 40 && time < 55 && halfTimeHappened == false){
				halfTimeHappened = true;
				System.out.println("\nHalf-time! Would you like to switch out? 1 for yes, 2 for no.");
				
				//Saving the answer from the scanner.
				int answer = sc.nextInt();
				
				//Checking the answer and acting accordingly
					if(answer == 1){
						
						//Printing out a list of the current players.
						System.out.println("Who would you like to switch out? (write the player number)");
						Player[] teamList = t1.getPlayers();
						for(int i = 0; i < 10; i++)
							System.out.println(i+1 + " " + teamList[i].getName());
						
						//Saving the number of the switched out player.
						int switchedOutPlayer = sc.nextInt();
						
						//Printing out a list of available players and saving the answer in the scanner.
						System.out.println("\nWho would you like to switch in?");
						for(int i = 10; i < 11; i++)
							System.out.println(i+1 + " " + teamList[i].getName());
						int switchedInPlayer = sc.nextInt();
						
						//Taking the old player and replacing it with the new.
						t1.getPlayers()[switchedOutPlayer-1] = t1.getPlayers()[switchedInPlayer-1];
						
						//Printing out the new player list.
						System.out.println("New player list:");
						for(int i = 0; i < 10; i++)
							System.out.print("\n" + (i+1) + " " + teamList[i].getName());
						
					}
					//The user answered "no", the game will continue.
					else if (answer == 2)
						System.out.println("Continuing game without switch-out.");
					
					//The user entered an invalid answer, and they'll get slapped for that.
					else
						System.out.println("Invalid answer, you're weird. Continuing game.");
			}
		
			// influence of motivation on strength:
			float strength_1 = (t1.getStrength()/2.0f) + ((t1.getStrength()/2.0f)*(t1.getMotivation()/10.0f));
			float strength_2 = (t2.getStrength()/2.0f) + ((t2.getStrength()/2.0f) * (t2.getMotivation()/10.0f));
		
			// influence of trainer on strength:
			int deviation = r.nextInt(2);
			if (strength_1 > t1.getTrainer().getExperience())
				deviation = -deviation;
						//Math.max(1, x) means that even if x is a negative number,
						//it will become 1. You will never go below 1.
						//Math.min(10, y) means that even if y is less than 10,
						//it will become 10.
						//So these numbers will always be between 1 and 10.
			strength_1 = Math.max(1, Math.min(10, strength_1 + deviation));
			deviation = r.nextInt(2);
			if (strength_2 > t2.getTrainer().getExperience())
				deviation = -deviation;
			strength_2 = Math.max(1, Math.min(10, strength_2 + deviation));
		
			// randomly choose a player for next shot
			if ((r.nextInt(Math.round(strength_1+strength_2))-strength_1)<=0){
				teamShoots(t1, t2, time, true);
			} // IF 
			else{
				teamShoots(t2, t1, time, false);
			} // else 
		} //WHILE
	}
	
	//teamShoots(attacking team, defending team, time, are they homeTeam or not?)
	private void teamShoots(Team t1, Team t2, int time, boolean homeTeam){
		Random r = new Random();
		int comment = r.nextInt(5);
		int shooter = r.nextInt(10);
		
		//String-arrays with different comments
		String[] goalComment = new String[]{"GOOOOOAL!!", "Amazing score!", "HE SCORES! ALSO I SPILLED MY DRINK!!", "And it goes in the net!", "Did.. was there goal..? Technicians looking at i- there was a goal!!"};
		String[] saveComment = new String[]{t2.getKeeper().getName()+" saves brilliantly!", "But the ball is caught before it reaches the net!", "Amazing save from "+t2.getKeeper().getName()+"!", "BUT HE WAS DENIED!!", "But the lousy shot was easily saved by "+t2.getKeeper().getName()};
		
		Player p = t1.getPlayers()[shooter];
		Keeper k = t2.getKeeper();
		int shot = p.shootsOnGoal();
		// check if shot is saved
		boolean goal = !k.saveShot(shot);
		System.out.println();
		System.out.println(time+".Minute: ");
		System.out.println(" Chance for "+t1.getName()+" ...");
		System.out.println(" "+p.getName()+" shoots");
		if (goal) {
			if(homeTeam)
				pointsHome++;
			else
				pointsGuest++;
			p.addGoal();
			System.out.println(goalComment[comment]+ " " +pointsHome+":"+
			pointsGuest+" "+p.getName()+"("+p.getGoals()+")");
			} 
		else {
			System.out.println(saveComment[comment]);
		}
	}
	
}
