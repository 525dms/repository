//#include <iostream>

#include "GameObject.h"
#include <cmath>

GameObject::GameObject(float n1, float n2)
{
	x = n1;
	y = n2;
	angle = 0.0;
	power = 0.0;
}

GameObject::~GameObject()
{

}
