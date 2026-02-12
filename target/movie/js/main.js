// ==========================================
// CINEMA App - Main JavaScript
// Dark cinematic theme with smooth interactions
// ==========================================

(function() {
    'use strict';

    // Constants
    var RIPPLE_SIZE = 20;
    var RIPPLE_DURATION = 600;
    var SCROLL_THRESHOLD = 50;
    var OBSERVER_THRESHOLD = 0.1;

    // Wait for DOM to be ready
    document.addEventListener('DOMContentLoaded', function() {
        initHeaderScroll();
        initMovieCardInteractions();
        initSmoothScroll();
        initAnimations();
        initButtonEffects();
    });

    /**
     * Header scroll effect - add shadow and reduce padding on scroll
     */
    function initHeaderScroll() {
        var header = document.querySelector('.navbar');
        if (!header) return;

        window.addEventListener('scroll', function() {
            var currentScroll = window.pageYOffset;

            if (currentScroll > SCROLL_THRESHOLD) {
                header.classList.add('scrolled');
            } else {
                header.classList.remove('scrolled');
            }
        }, { passive: true });
    }

    /**
     * Movie card click interactions
     */
    function initMovieCardInteractions() {
        var movieCards = document.querySelectorAll('.movie-card');

        movieCards.forEach(function(card) {
            // Add ripple effect on click
            card.addEventListener('click', function(e) {
                if (e.target.closest('.btn')) return;

                createRippleEffect(card, e);
            });

            // Add keyboard accessibility
            card.setAttribute('tabindex', '0');
            card.setAttribute('role', 'button');

            card.addEventListener('keydown', function(e) {
                if (e.key === 'Enter' || e.key === ' ') {
                    e.preventDefault();
                    card.click();
                }
            });
        });
    }

    /**
     * Create ripple effect on element
     */
    function createRippleEffect(element, event) {
        var ripple = document.createElement('span');
        ripple.style.cssText =
            'position: absolute;' +
            'width: ' + RIPPLE_SIZE + 'px;' +
            'height: ' + RIPPLE_SIZE + 'px;' +
            'background: rgba(212, 175, 55, 0.3);' +
            'border-radius: 50%;' +
            'transform: scale(0);' +
            'pointer-events: none;' +
            'z-index: 10;';

        var rect = element.getBoundingClientRect();
        var x = event.clientX - rect.left;
        var y = event.clientY - rect.top;

        ripple.style.left = x + 'px';
        ripple.style.top = y + 'px';

        element.style.position = 'relative';
        element.style.overflow = 'hidden';
        element.appendChild(ripple);

        ripple.animate([
            { transform: 'scale(0)', opacity: 1 },
            { transform: 'scale(20)', opacity: 0 }
        ], {
            duration: RIPPLE_DURATION,
            easing: 'cubic-bezier(0.4, 0, 0.2, 1)'
        }).onfinish = function() {
            ripple.remove();
        };
    }

    /**
     * Smooth scroll for anchor links
     */
    function initSmoothScroll() {
        document.querySelectorAll('a[href^="#"]').forEach(function(anchor) {
            anchor.addEventListener('click', function(e) {
                var targetId = anchor.getAttribute('href').slice(1);
                var target = document.getElementById(targetId);

                if (target) {
                    e.preventDefault();
                    target.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            });
        });
    }

    /**
     * Initialize intersection observer for scroll animations
     */
    function initAnimations() {
        if ('IntersectionObserver' in window) {
            var observer = new IntersectionObserver(function(entries) {
                entries.forEach(function(entry) {
                    if (entry.isIntersecting) {
                        entry.target.classList.add('animate-in');
                        observer.unobserve(entry.target);
                    }
                });
            }, {
                threshold: OBSERVER_THRESHOLD,
                rootMargin: '0px 0px -50px 0px'
            });

            // Observe sections
            document.querySelectorAll('section').forEach(function(section) {
                observer.observe(section);
            });

            // Observe movie cards for stagger animation
            var movieGrid = document.querySelector('.movie-grid');
            if (movieGrid) {
                movieGrid.querySelectorAll('.movie-card').forEach(function(card, index) {
                    card.style.animationDelay = (index * 0.1) + 's';
                    card.classList.add('animate-card');
                });
            }
        }
    }

    /**
     * Button hover effects
     */
    function initButtonEffects() {
        var buttons = document.querySelectorAll('.btn');

        buttons.forEach(function(button) {
            button.addEventListener('mouseenter', function() {
                this.style.transform = 'translateY(-2px)';
            });

            button.addEventListener('mouseleave', function() {
                this.style.transform = 'translateY(0)';
            });
        });
    }

    /**
     * Utility: Debounce function
     */
    function debounce(func, wait) {
        var timeout;
        return function executedFunction() {
            clearTimeout(timeout);
            timeout = setTimeout(function() {
                func.apply(this, arguments);
            }, wait);
        };
    }

    /**
     * Utility: Get cookie value
     */
    function getCookie(name) {
        var value = '; ' + document.cookie;
        var parts = value.split('; ' + name + '=');
        if (parts.length === 2) return parts.pop().split(';').shift();
        return null;
    }

    /**
     * Utility: Set cookie
     */
    function setCookie(name, value, days) {
        var expires = new Date(Date.now() + days * 864e5).toUTCString();
        document.cookie = name + '=' + encodeURIComponent(value) + '; expires=' + expires + '; path=/';
    }

    /**
     * Utility: Throttle function
     */
    function throttle(func, limit) {
        var inThrottle;
        return function() {
            if (!inThrottle) {
                func.apply(this, arguments);
                inThrottle = true;
                setTimeout(function() { inThrottle = false; }, limit);
            }
        };
    }

    // Expose utility functions globally
    window.CINEMA = {
        debounce: debounce,
        throttle: throttle,
        getCookie: getCookie,
        setCookie: setCookie,
        createRipple: createRippleEffect
    };

})();
