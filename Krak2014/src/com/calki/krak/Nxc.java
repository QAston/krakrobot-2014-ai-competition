package com.calki.krak;

public class Nxc {

	enum WiezaTyp
	{
	    WIEZA_BRAK,
	    WIEZA_NIEBIESKA,
	    WIEZA_BIALA
	}

	/*enum RelativeDirection
	{
	    FRONT = 0,
	    LEFT,
	    RIGHT,
	    BACK
	};
	                                  // global orientation  rel of which to calc
	char relativeAsGlobal[4][4] =
	{
	// north
	 {NORTH,  WEST, EAST, SOUTH},
	 // south
	 {SOUTH,  EAST, WEST, NORTH},
	 // east
	 {EAST,  NORTH, SOUTH, WEST},
	// west
	 {WEST,  SOUTH, NORTH, EAST},
	};

	struct Pole
	{
	    bool znane;
	    char wieza;
	};

	Queue_char polecenia;
	Position aktualnaPozycja;
	char aktualnaOrientacja;
	char czyWracamy;
	Pole plansza[PLANSZA_ROZMIAR*PLANSZA_ROZMIAR];

	bool pole_canEnter(const Pole  pole)
	{
	   return !pole.znane || pole.wieza == WIEZA_BRAK;
	}

	void getNeighbours(Position& pos, char orientation, Position& outNeighbours[])
	{
	    for (char i = 0; i < 4; ++i)
	    {
	      outNeighbours[i] = position_getNeighbour(pos, relativeAsGlobal[orientation][i]);
	    }
	}

	char calcHeurystyka(Position pos, char powrot)
	{
	    if (powrot)
	       return 8 - pos.x - pos.y;
	    return pos.x + pos.y;
	}

	char kosztRuchu[] = {1, 2, 2, 3};

	char calcKoszt(char kierunekWzgledny)
	{
	    return kosztRuchu[kierunekWzgledny];
	}

	bool isGoal(Position pos, char powrot)
	{
	    if (powrot)
	        return pos.x == 4 && pos.y == 4;
	    return pos.x == 0 && pos.y == 0;
	}

	Position getGoal(char powrot)
	{
	     Position p;
	    if (powrot)
	    {
	      p.x = p.y = 4;
	    }
	    else
	      p.x = p.y = 0;

	    return p;
	}

	struct AstarNode
	{
	   Position pos;
	   char orientation;
	   char g;
	   char f;
	};

	bool minNode(AstarNode a, AstarNode b)
	{
	    return a.f < b.f || (a.f == b.f && a.g < b.g);
	}

	VECTOR(AstarNode)
	HEAP(AstarNode, minNode)

	struct AstarField
	{
	   Position fromWhere;
	   char moveOrientation;
	   bool visited;
	};

	// bug w NXC - nie mo¿na robiæ wielowymiarowych arrayów z podstrukturami
	AstarField fields[PLANSZA_ROZMIAR * PLANSZA_ROZMIAR];


	void search(Position& pos, char orientation, bool czyPowrot, Stos_char& outKierunkiRuchu)
	{
	    for(char i = 0; i < PLANSZA_ROZMIAR; ++i)
	        for(char j = 0; j < PLANSZA_ROZMIAR; ++j)
	        {
	            fields[CALC_INDEX(PLANSZA_ROZMIAR, i, j)].visited = false;
	        }
	    bool found = false;
	    bool resign = false;
	    
	    Vector_AstarNode open;
	    
	    vector_AstarNode_init(open, 15);

	    AstarNode current;
	    current.pos = pos;
	    current.orientation = orientation;
	    current.g = 0;
	    current.f = current.g + calcHeurystyka(pos, czyPowrot);

	    heap_AstarNode_minNode_insert(open, current);

	    AstarNode next;
	    /*
	    while (!found && !resign)
	    {
	        if (open.size == 0)
	        {
	            resign = 1;
	            return;
	        }
	        else
	        {
	            current = heap_AstarNode_minNode_removeMin(open);

	            if (isGoal(current.pos, czyPowrot))
	            {
	                found = 1;
	            }
	            else
	            {
	                for (char i = 0; i < 4; ++i)
	                {
	                    next.orientation = relativeAsGlobal[current.orientation][i];
	                    next.pos = position_getNeighbour(pos, next.orientation);
	                    if (position_isValid(next.pos))
	                    {
	                        int targetIndex = CALC_INDEX(PLANSZA_ROZMIAR, next.pos.x, next.pos.y);
	                        if (!fields[targetIndex].visited && pole_canEnter(plansza[targetIndex]))
	                        {
	                            next.g = current.g + calcKoszt(i);
	                            next.f = current.g + calcHeurystyka(pos, czyPowrot);
	                            heap_AstarNode_minNode_insert(open, next);

	                            fields[targetIndex].visited = true;
	                            fields[targetIndex].fromWhere = current.pos;
	                            fields[targetIndex].moveOrientation = i;
	                        }
	                    }
	                }
	            }
	        }
	    }

	    Position goal = getGoal(czyPowrot);
	    AstarField field;

	    while (!position_equals(goal, pos))
	    {
	        field = fields[CALC_INDEX(PLANSZA_ROZMIAR, goal.x, goal.y)];
	        stos_char_push(outKierunkiRuchu, field.moveOrientation);
	        goal = field.fromWhere;
	    } */
	}
/*
	enum Cmds
	{
	    CMD_JEDZ_PRZOD = 1,
	    CMD_OBROT_LEWO,
	    CMD_OBROT_PRAWO,
	    CMD_ZAKONCZ_PRZEJAZD,
	    CMD_WYMYSL_RUCHY
	};


	void wymyslRuchy()
	{

	    queue_char_makeEmpty(polecenia);
	    Stos_char kierunkiRuchu;
	    stos_char_init(kierunkiRuchu, 15);
	    
	    aktualnaPozycja.x = 4;
	    aktualnaPozycja.y = 4;
	    aktualnaOrientacja = 0;
	    czyWracamy = false;


	    search(aktualnaPozycja, aktualnaOrientacja, czyWracamy, kierunkiRuchu);

	    while(!stos_char_empty(kierunkiRuchu))
	    {
	        char kierunek = stos_char_top(kierunkiRuchu);
	        stos_char_pop(kierunkiRuchu);
	        switch(kierunek)
	        {
	           case FRONT:
	              queue_char_put(polecenia, CMD_JEDZ_PRZOD);
	              break;
	           case LEFT:
	              queue_char_put(polecenia, CMD_OBROT_LEWO);
	              queue_char_put(polecenia, CMD_JEDZ_PRZOD);
	              break;
	           case RIGHT:
	              queue_char_put(polecenia, CMD_OBROT_PRAWO);
	              queue_char_put(polecenia, CMD_JEDZ_PRZOD);
	              break;
	           case BACK:
	              queue_char_put(polecenia, CMD_OBROT_LEWO);
	              queue_char_put(polecenia, CMD_OBROT_LEWO);
	              queue_char_put(polecenia, CMD_JEDZ_PRZOD);
	              break;
	        }
	    }
	    queue_char_put(polecenia, CMD_ZAKONCZ_PRZEJAZD);
	}

	// ruch o jedno pole
	void jedzPrzod()
	{
	     OnRevSync(MOVE_MOTORS, 50, 0);
	     Wait(1500);
	     Off(MOVE_MOTORS);
	}

	void obrotLewo()
	{
	     OnRevSync(MOVE_MOTORS, 50, -200);
	     Wait(1500);
	     Off(MOVE_MOTORS);
	}

	void obrotPrawo()
	{
	     OnRevSync(MOVE_MOTORS, 50, +200);
	     Wait(1500);
	     Off(MOVE_MOTORS);
	}
}*/
