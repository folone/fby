# [fit] :wave:
# My name is @folone

![](https://cdn.eyeem.com/thumb/921cbec0c6081762b3ee7f5dcc85587c0b5716e1-1463689016783/3900/3900?highRes=true)

[https://github.com/folone/fby](https://github.com/folone/fby)

---

![](https://dl.dropboxusercontent.com/u/4274210/sc_cmyk_white.ai)

---

![fill inline](https://i1.sndcdn.com/avatars-000115643651-71o36c-t500x500.jpg)![fill inline](https://i1.sndcdn.com/artworks-000037484402-x8gfft-t500x500.jpg)![fill inline](https://i1.sndcdn.com/avatars-000138955484-r3yxsu-t500x500.jpg)![fill inline](https://i1.sndcdn.com/avatars-000207169396-qjliyo-t500x500.jpg)
![fill inline](https://i1.sndcdn.com/avatars-000109087465-2uqbx8-t500x500.jpg)![fill inline](https://i1.sndcdn.com/avatars-000055035285-1i17eh-t500x500.jpg)![fill inline](https://i1.sndcdn.com/avatars-000077357035-7tbvde-t500x500.jpg)![fill inline](https://i1.sndcdn.com/avatars-000170331047-7qtaxt-t500x500.jpg)

---

* 12 hours uploaded every minute
* ~35k listening years every month
* >135M tracks (including content from majors: Sony/Universal/Warner)
* ~180M monthly active users

![left](https://cdn.eyeem.com/thumb/c23be843844f1f0129727da2ed2f0a3991ca1347-1463689289498/3900/3900)

^ quite unique: major labes and dark underground
^ 4x spotify audio (>1PB vs >5PB)
^ 10x smaller team than spotify
^ a lot more interactive, as in the next slide
^ by a huge margin bigger than any competitor in both usage and amount of data (collection size)
^ except for youtube: that's bigger, but they aren't in the audio business per se

---

![](https://s3.amazonaws.com/f.cl.ly/items/0L1T380N1X3l262F3n3q/Image%202015-08-28%20at%203.51.58%20pm.png)

^ war stories, we have some fun challenges due to the social nature of soundcloud
^ Drake vs Meek Mill

---

![fill](https://dl.dropboxusercontent.com/u/4274210/sc_cmyk_white.ai)

:heart:

![fill](http://www.scala-lang.org/resources/img/smooth-spiral@2x.png)

^ story about rails app
^ also clojure/jruby

---

# [fit] Type level
# [fit] programming in Scala
:ghost:

![](http://www.scala-lang.org/resources/img/smooth-spiral@2x.png)

^ but i'm not here to talk about soundcloud

---

```scala
  sealed trait Bool {
    type &&[B <: Bool] <: Bool
    type ||[B <: Bool] <: Bool
    type IfElse[T, F] <: Any
  }
  trait True extends Bool {
    type &&[B <: Bool] = B
    type ||[B <: Bool] = True
    type IfElse[T, F] = T
  }
  trait False extends Bool {
    type &&[B <: Bool] = False
    type ||[B <: Bool] = B
    type IfElse[T, F] = F
  }
```

---

```scala
  // false || true == true
  implicitly[False # `||` [True] =:= True]

  // if(true) String else Int
  implicitly[True # IfElse[String, Int] =:= String]

  /* if(true) {
   *   if(false) Long else String
   * } else Int
   */
  implicitly[True # IfElse[False # IfElse[Long, String], Int] =:= String]
```

---

# [fit] Peano numbers!

![](https://cdn.eyeem.com/thumb/21316311450d6f2cf8bd1013d54ee1af7b66ec50-1463689008227/3900/3900)

---

```scala
  trait Nat
  trait Z extends Nat
  trait Succ[A <: Nat] extends Nat

```

---

```scala
  trait MinusOne[A <: Nat] {
    type Res <: Nat
  }

  object MinusOne {

    type Aux[A <: Nat, Res1 <: Nat] = MinusOne[A] { type Res = Res1 }

    implicit val baseCase: Aux[Z, Z] = new MinusOne[Z] {
      type Res = Z
    }
    implicit def inductiveCase[A <: Nat]: Aux[Succ[A], A] = new MinusOne[Succ[A]] {
      type Res = A
    }
  }

```

---

```scala
  trait Plus[A <: Nat, B <: Nat] {
    type Res <: Nat
  }
  object Plus {
    type Aux[A <: Nat, B <: Nat, Res1 <: Nat] = Plus[A, B] { type Res = Res1 }

    implicit def baseCase[A <: Nat]: Aux[A, Z, A] = new Plus[A, Z] {
      type Res = A
    }
    implicit def inductiveCase[A <: Nat, B <: Nat, C <: Nat, D <: Nat]
      (implicit ev0: MinusOne.Aux[B, C],
                ev1: Plus.Aux[Succ[A], C, D]): Aux[A, B, D] =
      new Plus[A, B] {
        type Res = D
      }
  }

```

---

```scala
    implicit def inductiveCase[A <: Nat,
                               B <: Nat,
                               C <: Nat,
                               D <: Nat]
      (implicit ev0: MinusOne.Aux[B, C],
                ev1: Plus.Aux[Succ[A], C, D]): Aux[A, B, D] =
      new Plus[A, B] {
        type Res = D
      }

```

---

```scala
  type _1 = Succ[Z]
  type _2 = Succ[_1]
  type _3 = Succ[_2]

  implicitly[Plus.Aux[_1, _2, _3]]

```

---

# [fit] HLists!

![](https://cdn.eyeem.com/thumb/1ff38a057aadb82c1dfc1cbe16ede1b9e7ffac6a-1463689036886/3900/3900)

---

```scala
  trait Nat
  trait Z extends Nat
  trait Succ[A <: Nat] extends Nat

  trait HList
  trait HNil extends HList
  trait HCons[A, T <: HList] extends HList
```

---

```scala
  trait Length[L <: HList] {
    type Res <: Nat
  }

  object Length {
    type Aux[L <: HList, Res1 <: Nat] = Length[L] { type Res = Res1 }
    implicit val baseCase: Aux[HNil.type, Z] = new Length[HNil.type] {
      type Res = Z
    }
    implicit def inductiveCase[H, T <: HList, N <: Nat]
      (implicit ev0: Length.Aux[T, N]) =
      new Length[HCons[H, T]] {
        type Res = Succ[N]
      }
  }
```

---

# [fit] Shapeless!
https://github.com/milessabin/shapeless

![](https://cdn.eyeem.com/thumb/2bf9873154a6d5eafe53620196965235f236b07d-1463689176082/3900/3900)

---

![](https://c2.staticflickr.com/4/3495/3261725397_c68586ccf4_b.jpg)

---

# [fit] Typelog!
https://github.com/densh/typelog

![](https://cdn.eyeem.com/thumb/84bc8c92672a53e443d380491e7ce51377dfaae8-1463689348089/3900/3900)

---

# [fit] Json

![](https://cdn.eyeem.com/thumb/e0e19780f504d64d363429f70cd1261a398a5a6b-1463689172111/3900/3900)

---


# [fit] Danke!

![](https://cdn.eyeem.com/thumb/50eb5ca935272637b24bebb4bae05883f2046961-1463689144923/3900/3900)

@folone

[https://soundcloud.com/jobs](https://soundcloud.com/jobs)
[https://github.com/folone/fby](https://github.com/folone/fby)
