# Code guidelines

Code is a form of interface where the computer is obviously part of your audience, but so are other developers. Both are equally important.

It's hard to make a computer understand your code, it's even harder to make a human understand it.

## No inline style tags

For example, `<h2 style="color: white;">`)

If HTML is the model, CSS is the view, and JS is the controller. Don't mix them.

Styles should be contained within their own files to promote better organization and easier maintenance.

Prefer library styles over custom styles. You wouldn't recreate a sort function if you already include a library function that does it.

If you have to write a custom style, use class attributes.

## No checked in commented code

Git keeps a full history, so unless there are *immediate* plans to uncomment, there's no reason to keep unused code around cluttering it up.

Make reusable components/files -- Don't Repeat Yourself (DRY)

Unless it is substantially more code to do so, code (especially HTML) should not be repeated. This allows for easier maintenance and less copy/paste related bugs. Think of components like functions.

# Styling guidelines

## Consistency is key

First and foremost, be consistent. The more consistent you are, the less lessons the user has to learn and keep in their head (cognitive load) while using your interface.

That being said, do not overload the user. Too much at once or too much "cognitive load" causes the user to become confused, annoyed, and stressed. You want your user to *enjoy* using your interface, not loathe it.

Each screen or view should have a distinct purpose. The general test is if you can't sum up what the screen does in one or two words (e.g. "user management"), then it likely needs to be broken up.

Do not fall victim to the trap of counting "number of clicks". In general, less clicks is better, but clear organization and easy understanding (i.e. "intuitiveness") is far more important and often requires more clicks.

## Don't be afraid of whitespace!

A good UI uses whitespace effectively to emphasize or to organize.

Think of how you might arrange a shelf. A little bit of a gap between boxes or books goes a long way to show the imaginary compartments you've created on the shelf.

Something that looks sparse to the original developer can still look information dense and cluttered to a user -- especially their first time using it.

The more things you have on screen (this even includes things like borders or background images), the more cluttered your interface looks and the more cognitive load it creates.

Everything about your interface should be communicating *something* relevant to the user (even if it is communicating something more abstract like how the interface is organized).

## Color usage

Colors should not be the only way to obtain a piece of information (colorblindness is very common), but should still be used to augment the user's experience.

For example, you should have a primary color to draw the user's attention to the "happy path" (see CTAs) button. You'll use more warm colors to indicate "danger" or a negative response (e.g. green means affrimative, red means negative).

## Calls to Action (CTA)

Primary actions (AKA "calls to action" or CTA) should be at the very far left or right (which one is down to style, just be consistent). Again, this is to draw the user to them quickly.

For example, if the user can cancel or submit a form, then the submit button is (likely) the CTA and should be on the "outside" (far left or far right) of the form.

## Tables

Tables are a great way to organize *tabular data*.

Only include columns that are immediately relevant to the task at hand (tables can almost be thought of as "sub screens").

One view should only have one or two tables visible at the same time, *at most*.

Tables are extremely information dense -- use this to your advantage but be cautious.
