(setq a t)
(setq b 23)
(defun igual (x y) (equal x y) )
(cond ((> 2 3)(setq b 23)) ((equal (list 1 2 3) (list 1 2 3)) (setq b 24)) )
(print (+ 1 (cond (nil (print "prueba") 2) (a (print "yes") 3)) ))
(write "El valor de b es: ")
(setq b (atom 'foo))
(print (igual "2" "2"))