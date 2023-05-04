%% Problem 1.1
clc;
c = [13;
    11];

A = [4 5;
    5 3;
    1 2];

b = [1500;
    1575;
    420];

c = -c;

lb =[0;
    0];

%[x, fval, exitflag, output, lambda]= linprog(c, A, b, Aeq, beq, lb, ub,
%x0, options)%

%options = optimoptions('linprog', 'Algorithm', 'dual-simplex', 'Display', 'iter');
%options = optimoptions('linprog', 'Algorithm', 'dual-simplex', 'Display', 'off');
options = optimoptions('linprog', 'Algorithm', 'interior-point', 'Display', 'iter');
%options = optimoptions('linprog', 'Algorithm', 'interior-point', 'Display', 'off');

[x, fval_p, exitflag, output, lambda] = linprog(c, A, b,[],[],lb,[],[], options);
x
%% Problem 1.2

  clc;
  
  c = [1500;
    1575;
    420];

   A = [4 5 1;
    5 3 2];

   b = [13;
       11];
   
   lb = [0;
      0;
      0];
  
  %The second inequality can be changed to the less-than-or-equal-
  %to type by multiplying both sides of the inequality by -1 and
  %reversing the direction of the inequality; that is
  A = -A;
  b = -b,
  
  
   
options = optimoptions('linprog', 'Algorithm', 'dual-simplex', 'Display', 'iter');
%options = optimoptions('linprog', 'Algorithm', 'dual-simplex', 'Display', 'off');
%options = optimoptions('linprog', 'Algorithm', 'interior-point', 'Display', 'iter');
%options = optimoptions('linprog', 'Algorithm', 'interior-point', 'Display', 'off');
  
[x, fval_d, exitflag, output, lambda] = linprog(c, A, b,[],[],lb,[],[],options);

x
%% COMPARE
fprintf("Primal optimal value: %f\n", -1*fval_p);
fprintf("Dual optimal value: %f\n", fval_d);
%% Task 2.1

clc;
c = [1;
    1;
    1;
    1;
    1;
    1;
    1];

A = [1 0 0 1 1 1 1;
     1 1 0 0 1 1 1;
     1 1 1 0 0 1 1;
     1 1 1 1 0 0 1;
     1 1 1 1 1 0 0;
     0 1 1 1 1 1 0;
     0 0 1 1 1 1 1];

b = [8;
     6;
     5;
     4;
     6;
     7;
     9];


lb =[0;
     0;
    0;
    0;
    0;
    0;
    0];

intcon = [1;
          2;
          3;
          4;
          5;
          6;
          7];
      
 A = -A;
 b = -b;


options = optimoptions('linprog', 'Display', 'off');
[x, fval, exitflag, output] = linprog(c, A, b, [], [], lb, [], options);


%% Excercise 2.2

clc;
c = [1;
    1;
    1;
    1;
    1;
    1;
    1];

A = [1 0 0 1 1 1 1;
     1 1 0 0 1 1 1;
     1 1 1 0 0 1 1;
     1 1 1 1 0 0 1;
     1 1 1 1 1 0 0;
     0 1 1 1 1 1 0;
     0 0 1 1 1 1 1];

b = [8;
     6;
     5;
     4;
     6;
     7;
     9];


lb =[0;
     0;
    0;
    0;
    0;
    0;
    0];

intcon = [1;
          2;
          3;
          4;
          5;
          6;
          7];
      
 A = -A;
 b = -b;
 
options = optimoptions('intlinprog', 'Display', 'off');
[x, fval, exitflag, output] = intlinprog(c, intcon, A, b, [], [], lb, [], options);