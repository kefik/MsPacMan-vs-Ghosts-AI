Refactored MsPacMan-vs-Ghosts code v2.1.2 from competition: https://www.facebook.com/pacman.vs.ghosts (competition webpage is already dead)

Current version: 2.2.0

PacMan-vs-Ghosts -> main project containing PacManSimulator (runnable as is), which is featuring keyboard-controlled Ms PacMan, use arrows, 'P' to pause the game, 'N' to advance the game 1 frame

PacMan-vs-Ghosts-Agents -> example agents (both Ms PacMan and Ghosts), this project must reference PacMan-vs-Ghosts in order to compile ... start to develop your own agent by editting any of example agents

PacMan-vs-Ghosts-Tournament -> console app that can be used to quickly evaluate agents (multiple seeds, multiple repetitions, output results to CSV)

FEATURES:

-- Fully working PacManSimulator that can run visualized / headless
-- PacMan controller can be hijacked (press 'H' and manually navigate Pac-Man) or the simulation paused with 'P' and then advance frame by frame via 'N'
-- access the level info via Maze module and more OOP way than "indices/C-like getters"
-- fixed bug in PacManReplayer, use 'P'ause, 'N'ext frame, and 'Z' to make replay run faster or 'X' to make replay run slower
-- Visualizer can be Scale2x
-- draw custom debug info via GameView static methods
-- you can run the game WITHOUT ghosts as well (in order to play with pathfinding only)
-- you can use console app to quickly evaluate your agents

EXERCISES:

The project is/was used to teach basic AI techniques (mainly graph-searches). It contains exercises to be given to students
including visualization of graph-search algorithm execution.

==========
CHANGE LOG
==========

v 2.2.0

- CODE MOVED TO MY OWN REPOSITORY: https://github.com/kefik/MsPacMan-vs-Ghosts-AI
- changed the way images and text files are loaded (now they are loaded as resources)
- reworked Exec into PacManSimulator & PacManReplayer
- you can run the game WITHOUT ghosts as well (in order to play with pathfinding only)
- fixed native ghosts bug (infinite loop)
- PacManReplayer is fully working! Issue from 2.1.2 solved by explicitly logging node indices (positions) of Ms Pac-Man & Ghosts
- GameView allows to display DebugTexts as well
- Example agents moved to separate project PacMan-vs-Ghosts-Agents
- Direction enum created
- PacMan & Ghosts actions are now transfered in PacManAction & GhostsActions objects rather than numbers
- Maze module created that allows you to access the maze in an OOP way
- PacManHijackController created that allows you to pause/advance single frame/hijack PacMan controls at any time (great for debugging!)
- Visualization now contains Scale2x mode (taken from MarioAI, kudos to Sergey Karakovskiy), which is enabled as default
- starter package deleted (all example agents contain main() methods so they are directly runnable / modifiable / copy-paste ...)
- the simulation features TIMED execution of Pac-Man/Ghosts logic only (but you can always set high thinking time as there is no "fixed" waiting)

v 2.1.2

- changed the way the images and text files are loaded to allow applet version to work
- added a way to _G_ to record games for javascript replays on web-site
- localised file-name descriptors
- added the following helper methods:
	- public int getNextEdibleGhostScore();
	- public int getNumActivePills();
	- public int getNumActivePowerPills();
	- public int[] getPillIndicesActive();
	- public int[] getPowerPillIndicesActive();
- updated the sample controllers to use the new functions
- added boolean flag to GameView to prevent unnecessary storing of information
- use StringBuilder to save replays
- updated recording feature to flush string after each save
- include starter package in code distribution

known issues:

- bug in recording replays: the ghost update method (every nth game tick) causes the replay to crash whenever a power pill has been eaten). This is caused by the lack of update for edible ghosts. Fix in progress.

v 2.1.1

- changed the graphics in GameView to do double buffering
- added the ability to do simple visuals for debugging/testing/demonstrations
- changed the way a game is initialised: removed the singleton pattern
- added NearestPillPacManVS to illustrate the visuals
- added 5 (utility) methods in Game/G:
	- public int[] getPath(int from,int to);
	- public int getTarget(int from,int[] targets,boolean nearest,DM measure);
	- public int[] getGhostPath(int whichGhost,int to);
	- public int getGhostPathDistance(int whichGhost,int to);
	- public int getGhostTarget(int from,int[] targets,boolean nearest);
- changelog now included in source code distribution
	
v 2.1.0

- fixed the creation of the junction array in the class Node (changed from >3 to >2)
- changed the spelling from juntionIndices to junctionIndices
- added 2 methods to Game (and G) to get all possible directions for Ms Pac-Man and the ghosts as an array
	- changed the sample controllers accordingly
	- changed the game core accordingly
- removed Random from G (can use it from Game)
- changed advanceGame(-) to return the actual actions taken (primarily for replays)
- fixed the replay mechanism which was buggy in some cases where a life was lost
- added a sample experimental setup to Exec to illsutrate how to run many games efficiently
- fixed nearestPillPac-Man to include search for power pills
- changed the way ghost reversals are done (now using a Boolean flag)
- added more comments to source code, especially in Game	
	
v 2.0.2

- fixed the isJunction function which now checks for more than 2 options, not 3 (thanks to Daryl)
	
v 2.0.1

- fixed the speed of the ghosts when edible - now they move more slowly, as before (thanks to Kien)
- the scores obtained for eating ghosts in succession was incorrect - now it is 200-400-800-1600 (thanks to Kien)
- added the ability to record and replay games by saving the actions taken by the controllers	
	
v 2.0.0

- complete revamp of the code. Please see documentation on the website for information regarding the code