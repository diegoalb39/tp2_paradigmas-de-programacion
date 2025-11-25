:- dynamic artista/3.       % nombre, tipo(base/invitado), costo
:- dynamic artista_rol/2.   % artista,rol
:- dynamic cancion_rol/2.   % cancion,rol requerido

% cantidad_de_roles(Cancion, Rol, Cant):
% Cuenta cuántas veces una canción requiere un rol.
cantidad_de_roles(Cancion, Rol, Cant) :-
    findall(1, cancion_rol(Cancion, Rol), L),
    length(L, Cant).

% bases_que_pueden(Rol, Cant):
% Cuenta cuántos artistas base conocen ese rol.
bases_que_pueden(Rol, Cant) :-
    findall(1,
        (artista_rol(A, Rol), artista(A, base, _)),
        L),
    length(L, Cant).

% roles_sin_cubrir(Cancion, Rol, Faltantes):
% faltantes - lo que pueden cubrir los bases.
roles_sin_cubrir(Cancion, Rol, Faltantes) :-
    cantidad_de_roles(Cancion, Rol, Req),
    bases_que_pueden(Rol, Cubren),
    Temp is Req - Cubren,
    (Temp > 0 -> Faltantes = Temp ; Faltantes = 0).

% entrenamientos_minimos(Total):
% Suma los faltantes sin cubrir por bases en todas las canciones.
entrenamientos_minimos(Total) :-
    setof((C,R), cancion_rol(C,R), Instancias),
    findall(F,
        (member((C,R), Instancias),
         roles_sin_cubrir(C,R,F)),
        Lista),
    sumlist(Lista, Total).   